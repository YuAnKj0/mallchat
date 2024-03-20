package com.yuankj.mallchat.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.sun.xml.internal.bind.v2.TODO;
import com.yuankj.mallchat.chat.dao.ContactDao;
import com.yuankj.mallchat.chat.dao.GroupMemberDao;
import com.yuankj.mallchat.chat.dao.MessageDao;
import com.yuankj.mallchat.chat.domain.dto.RoomBaseInfo;
import com.yuankj.mallchat.chat.domain.entity.*;
import com.yuankj.mallchat.chat.domain.enums.GroupRoleAPPEnum;
import com.yuankj.mallchat.chat.domain.enums.HotFlagEnum;
import com.yuankj.mallchat.chat.domain.enums.RoomTypeEnum;
import com.yuankj.mallchat.chat.domain.vo.response.ChatRoomResp;
import com.yuankj.mallchat.chat.domain.vo.response.MemberResp;
import com.yuankj.mallchat.chat.service.RoomAppService;
import com.yuankj.mallchat.chat.service.RoomService;
import com.yuankj.mallchat.chat.service.adapter.ChatAdapter;
import com.yuankj.mallchat.chat.service.cache.HotRoomCache;
import com.yuankj.mallchat.chat.service.cache.RoomCache;
import com.yuankj.mallchat.chat.service.cache.RoomFriendCache;
import com.yuankj.mallchat.chat.service.cache.RoomGroupCache;
import com.yuankj.mallchat.chat.service.strategy.msg.AbstractMsgHandler;
import com.yuankj.mallchat.chat.service.strategy.msg.MsgHandlerFactory;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.user.dao.UserDao;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.service.cache.UserCache;
import com.yuankj.mallchat.user.service.cache.UserInfoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-03-07/11:11
 * @apiNote
 */
@Service
public class RoomAppServiceImpl implements RoomAppService {
	
	@Autowired
	private ContactDao contactdao;
	@Autowired
	private HotRoomCache hotRoomCache;
	@Autowired
	private RoomCache roomCache;
	@Autowired
	private RoomGroupCache roomGroupCache;
	@Autowired
	private RoomFriendCache roomFriendCache;
	@Autowired
	private UserInfoCache userInfoCache;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private RoomService roomService;
	@Autowired
	private UserCache userCache;
	@Autowired
	private GroupMemberDao groupMemberDao;
	@Autowired
	private UserDao userDao;
	/**
	 * @param request 
	 * @param uid
	 * @return
	 */
	@Override
	public CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq request, Long uid) {
		//查询出用户要展示的会话列表
		CursorPageBaseResp<Long> page;
		if (Objects.nonNull(uid)) {
			Double hotEnd = getCursorOrNull(request.getCursor());
			Double hotStart = null;
			// 用户基础会话
			CursorPageBaseResp<Contact> contactPage = contactdao.getContactPage(uid, request);
			List<Long> baseRoomIds = contactPage.getList().stream().map(Contact::getRoomId).collect(Collectors.toList());
			if (!contactPage.getIsLast()) {
				hotStart=getCursorOrNull(contactPage.getCursor());
			}
			// 热门房间
			Set<ZSetOperations.TypedTuple<String>> typedTuples = hotRoomCache.getRoomRange(hotStart, hotEnd);
			List<Long> hotRoomIds = typedTuples.stream().map(ZSetOperations.TypedTuple::getValue).filter(Objects::nonNull).map(Long::parseLong).collect(Collectors.toList());
			baseRoomIds.addAll(hotRoomIds);
			//基础会话和热门房间合并
			page=CursorPageBaseResp.init(contactPage,baseRoomIds);
		}else {
			//用户未登录，只查全局房间
			CursorPageBaseResp<Pair<Long, Double>> roomCursorPage = hotRoomCache.getRoomCursorPage(request);
			
			List<Long> roomIds = roomCursorPage.getList().stream().map(Pair::getKey).collect(Collectors.toList());
			page=CursorPageBaseResp.init(roomCursorPage,roomIds);
		}
		if (CollectionUtil.isEmpty(page.getList())){
			return CursorPageBaseResp.empty();
		}
		//最后组装会话信息(名称头像未读数等)
		List<ChatRoomResp> result =buildContactResp(uid,page.getList());
		return CursorPageBaseResp.init(page,result);
		
	}
	@NotNull
	private List<ChatRoomResp> buildContactResp(Long uid, List<Long> roomIds) {
		//表情和头像
		Map<Long, RoomBaseInfo> roomBaseInfoMap=getRoomBaseInfoMap(roomIds,uid);
		// 最后一条消息
		List<Long> msgIds = roomBaseInfoMap.values().stream().map(RoomBaseInfo::getLastMsgId).collect(Collectors.toList());
		List<Message> messages = CollectionUtil.isEmpty(msgIds) ? new ArrayList<>() : messageDao.listByIds(msgIds);
		Map<Long, Message> msgMap = messages.stream().collect(Collectors.toMap(Message::getId, Function.identity()));
		Map<Long, User> lastMsgUidMap = userInfoCache.getBatch(messages.stream().map(Message::getFromUid).collect(Collectors.toList()));
		// 消息未读数
		Map<Long, Integer> unReadCountMap = getUnReadCountMap(uid, roomIds);
		return roomBaseInfoMap.values().stream().map(room -> {
					ChatRoomResp resp = new ChatRoomResp();
					RoomBaseInfo roomBaseInfo = roomBaseInfoMap.get(room.getRoomId());
					resp.setAvatar(roomBaseInfo.getAvatar());
					resp.setRoomId(room.getRoomId());
					resp.setActiveTime(room.getActiveTime());
					resp.setHot_Flag(roomBaseInfo.getHotFlag());
					resp.setType(roomBaseInfo.getType());
					resp.setName(roomBaseInfo.getName());
					Message message = msgMap.get(room.getLastMsgId());
					if (Objects.nonNull(message)) {
						AbstractMsgHandler strategyNoNull = MsgHandlerFactory.getStrategyNoNull(message.getType());
						resp.setText(lastMsgUidMap.get(message.getFromUid()).getName() + ":" + strategyNoNull.showContactMsg(message));
					}
					resp.setUnreadCount(unReadCountMap.getOrDefault(room.getRoomId(), 0));
					return resp;
				}).sorted(Comparator.comparing(ChatRoomResp::getActiveTime).reversed())
				.collect(Collectors.toList());
		
	}
	private Map<Long, Integer> getUnReadCountMap(Long uid, List<Long> roomIds) {
		if (Objects.isNull(uid)) {
			return new HashMap<>();
		}
		List<Contact> contacts = contactdao.getByRoomIds(roomIds, uid);
		return contacts.parallelStream()
				.map(contact -> Pair.of(contact.getRoomId(), messageDao.getUnReadCount(contact.getRoomId(), contact.getReadTime())))
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}
	
	
	private Map<Long, RoomBaseInfo> getRoomBaseInfoMap(List<Long> roomIds, Long uid) {
		Map<Long, Room> roomMap = roomCache.getBatch(roomIds);
		//房间根据好友和群组类型分组
		Map<Integer, List<Long>> groupRoomIdMap = roomMap.values().stream().collect(Collectors.groupingBy(Room::getType, Collectors.mapping(Room::getId, Collectors.toList())));
		//获取群组信息
		List<Long> groupRoomId = groupRoomIdMap.get(RoomTypeEnum.GROUP.getType());
		Map<Long, RoomGroup> roomInfoBatch = roomGroupCache.getBatch(groupRoomId);
		
		//获取好友信息
		List<Long> friendRoomId = groupRoomIdMap.get(RoomTypeEnum.FRIEND.getType());
		Map<Long, User> friendRoomMap =getFriendRoomMap(friendRoomId,uid);
		return roomMap.values().stream().map(room -> {
			RoomBaseInfo roomBaseInfo = new RoomBaseInfo();
			roomBaseInfo.setRoomId(room.getId());
			roomBaseInfo.setType(room.getType());
			roomBaseInfo.setHotFlag(room.getHotFlag());
			roomBaseInfo.setLastMsgId(room.getLastMsgId());
			roomBaseInfo.setActiveTime(room.getActiveTime());
			if (RoomTypeEnum.of(room.getType()) == RoomTypeEnum.GROUP) {
				RoomGroup roomGroup = roomInfoBatch.get(room.getId());
				roomBaseInfo.setName(roomGroup.getName());
				roomBaseInfo.setAvatar(roomGroup.getAvatar());
			} else if (RoomTypeEnum.of(room.getType()) == RoomTypeEnum.FRIEND) {
				User user = friendRoomMap.get(room.getId());
				roomBaseInfo.setName(user.getName());
				roomBaseInfo.setAvatar(user.getAvatar());
			}
			return roomBaseInfo;
		}).collect(Collectors.toMap(RoomBaseInfo::getRoomId, Function.identity()));
		
	}
	
	private Map<Long, User> getFriendRoomMap(List<Long> roomIds, Long uid) {
		if (CollectionUtil.isEmpty(roomIds)) {
			return new HashMap<>();
		}
		Map<Long, RoomFriend> roomFriendMap =roomFriendCache.getBatch(roomIds);
		Set<Long> friendUidSet = ChatAdapter.getFriendUidSet(roomFriendMap.values(),uid);
		Map<Long, User> userBatch = userInfoCache.getBatch(new ArrayList<>(friendUidSet));
		return roomFriendMap.values()
				.stream()
				.collect(Collectors.toMap(RoomFriend::getRoomId, roomFriend -> {
					Long friendUid = ChatAdapter.getFriendUid(roomFriend, uid);
					return userBatch.get(friendUid);
				}));
	}
	
	/**
	 * @param uid 
	 * @param id
	 * @return
	 */
	@Override
	public ChatRoomResp getContactDetail(Long uid, long roomId) {
		Room room = roomCache.get(roomId);
		AssertUtil.isNotEmpty(room, "房间号不存在" );
		return buildContactResp(uid, Collections.singletonList(roomId)).get(0);
	}
	
	/**
	 * @param uid 
	 * @param friendUid
	 * @return
	 */
	@Override
	public ChatRoomResp getContactDetailByFriend(Long uid, Long friendUid) {
		RoomFriend friendRoom =roomService.getFriendRoom(uid,friendUid);
		AssertUtil.isNotEmpty(friendRoom, "他不是您的好友");
		return buildContactResp(uid, Collections.singletonList(friendRoom.getRoomId())).get(0);
	}
	
	/**
	 * @param uid 
	 * @param roomId
	 * @return
	 */
	@Override
	public MemberResp getGroupDetail(Long uid, long roomId) {
		RoomGroup roomGroup = roomGroupCache.get(roomId);
		Room room = roomCache.get(roomId);
		AssertUtil.isNotEmpty(roomGroup, "ID有误");
		Long onlineNum;
		if(isHotGroup(room)){
			// 热点群从redis取人数
			onlineNum = userCache.getOnlineNum();
		}else {
			List<Long> memberUidList = groupMemberDao.getMemberUidList(roomGroup.getId());
			onlineNum=userDao.getOnlineCount(memberUidList).longValue();
		}
		GroupRoleAPPEnum groupRole=getGroupRole(uid, roomGroup, room);
		
		return MemberResp.builder()
				.avatar(roomGroup.getAvatar())
				.roomId(roomId)
				.groupName(roomGroup.getName())
				.onlineNum(onlineNum)
				.role(groupRole.getType()).build();
	}
	
	private GroupRoleAPPEnum getGroupRole(Long uid, RoomGroup roomGroup, Room room) {
		GroupMember member = Objects.isNull(uid) ? null : groupMemberDao.getMember(roomGroup.getId(), uid);
		if (Objects.nonNull(member)) {
			return GroupRoleAPPEnum.of(member.getRole());
		} else if (isHotGroup(room)) {
			return GroupRoleAPPEnum.MEMBER;
		} else {
			return GroupRoleAPPEnum.REMOVE;
		}
	}
	
	private boolean isHotGroup(Room room) {
		return HotFlagEnum.YES.getType().equals(room.getHotFlag());
	}
	
	private Double getCursorOrNull(String cursor) {
		return Optional.ofNullable(cursor).map(Double::parseDouble).orElse(null);
	}
}
