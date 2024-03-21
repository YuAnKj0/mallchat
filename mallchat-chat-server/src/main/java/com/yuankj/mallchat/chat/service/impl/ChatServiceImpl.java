package com.yuankj.mallchat.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import com.yuankj.mallchat.chat.dao.*;
import com.yuankj.mallchat.chat.domain.dto.MsgReadInfoDTO;
import com.yuankj.mallchat.chat.domain.entity.*;
import com.yuankj.mallchat.chat.domain.enums.MessageMarkActTypeEnum;
import com.yuankj.mallchat.chat.domain.enums.MessageTypeEnum;
import com.yuankj.mallchat.chat.domain.vo.request.chat.*;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberReq;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageReadResp;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.chat.service.ContactService;
import com.yuankj.mallchat.chat.service.adapter.MemberAdapter;
import com.yuankj.mallchat.chat.service.adapter.MessageAdapter;
import com.yuankj.mallchat.chat.service.adapter.RoomAdapter;
import com.yuankj.mallchat.chat.service.cache.RoomCache;
import com.yuankj.mallchat.chat.service.cache.RoomGroupCache;
import com.yuankj.mallchat.chat.service.helper.ChatMemberHelper;
import com.yuankj.mallchat.chat.service.strategy.mark.AbstractMsgMarkStrategy;
import com.yuankj.mallchat.chat.service.strategy.mark.MsgMarkFactory;
import com.yuankj.mallchat.chat.service.strategy.msg.AbstractMsgHandler;
import com.yuankj.mallchat.chat.service.strategy.msg.MsgHandlerFactory;
import com.yuankj.mallchat.chat.service.strategy.msg.RecallMsgHandler;
import com.yuankj.mallchat.common.annocation.RedissonLock;
import com.yuankj.mallchat.common.domain.enums.NormalOrNoEnum;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.event.MessageSendEvent;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.user.dao.UserDao;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.enums.ChatActiveStatusEnum;
import com.yuankj.mallchat.user.domain.enums.RoleEnum;
import com.yuankj.mallchat.user.domain.vo.response.ws.ChatMemberResp;
import com.yuankj.mallchat.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2023/10/9 0009/18:27
 * @apiNote
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
	public static final long ROOM_GROUP_ID = 1L;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private RoomCache roomCache;
	@Autowired
	private ContactDao contactDao;
	@Autowired
	private RoomFriendDao roomFriendDao;
	@Autowired
	private RoomGroupCache roomGroupCache;
	@Autowired
	private RoomGroupDao roomGroupDao;
	@Autowired
	private GroupMemberDao groupMemberDao;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private MessageMarkDao messageMarkDao;
    @Autowired
    private IRoleService iRoleService;
	@Autowired
	private RecallMsgHandler recallMsgHandler;
	@Autowired
	private ContactService contactService;
	@Autowired
	private UserDao userDao;
	
	
	/**
	 * @param request
	 * @param receiveUid
	 * @return {@code CursorPageBaseResp<ChatMessageResp>}
	 */
	@Override
	public CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, Long receiveUid) {
		//用最后一条消息id，来限制被踢出的人能看见的最大一条消息
		Long lastMsgId = getLastMsgId(request.getRoomId(), receiveUid);
		CursorPageBaseResp<Message> cursorPage = messageDao.getCursorPage(request.getRoomId(), request, lastMsgId);
		if (cursorPage.isEmpty()) {
			return CursorPageBaseResp.empty();
		}
		
		return CursorPageBaseResp.init(cursorPage, getMsgRespBatch(cursorPage.getList(), receiveUid));
	}
	
	private List<ChatMessageResp> getMsgRespBatch(List<Message> messages, Long receiveUid) {
		if (CollectionUtil.isEmpty(messages)) {
			return new ArrayList<>();
		}
		//查询消息标识
		List<MessageMark> msgMark = messageMarkDao.getValidMarkByMsgIdBatch(messages.stream().map(Message::getId).collect(Collectors.toList()));
		return MessageAdapter.buildMsgSave(messages, msgMark, receiveUid);
	}
	
	/**
	 * 发送消息
	 *
	 * @param request
	 * @param uid
	 * @return
	 */
	@Override
	@Transactional
	public Long sendMsg(ChatMessageReq request, Long uid) {
		check(request, uid);
		AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(request.getMsgType());
		Long msgId = msgHandler.checkAndSaveMsg(request, uid);
		//发布消息发送事件
		applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
		return msgId;
	}
	
	@Override
	public ChatMessageResp getMsgResp(Long msgId, Long receiveUid) {
		Message message = messageDao.getById(msgId);
		return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(message), receiveUid));
	}
	
	@Override
	public void setMsgMark(ChatMessageMarkReq request, Long uid) {
		AbstractMsgMarkStrategy strategy = MsgMarkFactory.getStrategyNoNull(request.getMarkType());
		switch (MessageMarkActTypeEnum.of(request.getActType())) {
			case MARK:
				strategy.mark(uid, request.getMsgId());
				break;
			case UN_MARK:
				strategy.unMark(uid, request.getMsgId());
				break;
		}
		
		
	}
    
    @Override
    public void recallMsg(ChatMessageBaseReq request, Long uid) {
        // 实现消息撤回逻辑
        Message message = messageDao.getById(request.getMsgId());
        // 检查消息是否可撤回
        checkRecall(uid, message);
        // 执行撤回操作
        recallMsgHandler.recall(uid,message);
    }
	
	@Override
	public CursorPageBaseResp<ChatMessageReadResp> getReadPage(@Nullable Long uid, ChatMessageReadReq request) {
		Message message = messageDao.getById(request.getMsgId());
		AssertUtil.isNotEmpty(message, "消息id不存在");
		AssertUtil.equal(uid,message.getFromUid(),"只能查看自己的消息");
		CursorPageBaseResp<Contact> page;
		//已读
		if (request.getSearchType()==1) {
			page = contactDao.getReadPage(message, request);
		}else{
			page=contactDao.getUnReadPage(message, request);
		}
		if (CollectionUtil.isEmpty(page.getList())) {
			return CursorPageBaseResp.empty();
		}
		return CursorPageBaseResp.init(page, RoomAdapter.buildReadResp(page.getList()));
	}
	
	@Override
	public Collection<MsgReadInfoDTO> getMsgReadInfo(Long uid, ChatMessageReadInfoReq request) {
		List<Message> messages = messageDao.listByIds(request.getMsgIds());
		messages.forEach(message -> {
			AssertUtil.equal(uid,message.getFromUid(),"只能查看自己的消息");
		});
		return contactService.getMsgReadInfo(messages).values();
	}
	
	/**
	 * @param uid 
	 * @param request
	 */
	@Override
	@RedissonLock(key = "#uid")
	public void msgRead(Long uid, ChatMessageMemberReq request) {
		Contact contact = contactDao.get(uid, request.getRoomId());
		if (Objects.nonNull(contact)) {
			Contact update = new Contact();
			update.setId(contact.getId());
			update.setReadTime(new Date());
			contactDao.updateById(contact);
		}else {
			Contact insert = new Contact();
			insert.setUid(uid);
			insert.setRoomId(request.getRoomId());
			insert.setReadTime(new Date());
			contactDao.save(insert);
		}
	}
	
	/**
	 * @param memberUidList 
	 * @param request
	 * @return
	 */
	@Override
	public CursorPageBaseResp<ChatMemberResp> getMemberPage(List<Long> memberUidList, MemberReq request) {
		Pair<ChatActiveStatusEnum, String> pair= ChatMemberHelper.getCursorPair(request.getCursor());
		ChatActiveStatusEnum activeStatusEnum = pair.getKey();
		String timeCursor = pair.getValue();
		List<ChatMemberResp> resultList=new ArrayList<>();//最终列表
		Boolean isLast = Boolean.FALSE;
		if (activeStatusEnum== ChatActiveStatusEnum.ONLINE) {
			//在线列表
			CursorPageBaseResp<User> cursorPage=userDao.getCursorPage(memberUidList,new CursorPageBaseReq(request.getPageSize(), timeCursor),ChatActiveStatusEnum.ONLINE);
			resultList.addAll(MemberAdapter.buildMember(cursorPage.getList()));
			timeCursor=cursorPage.getCursor();
			isLast=cursorPage.getIsLast();
		}
		//获取成员角色id
		List<Long> uidList = resultList.stream().map(ChatMemberResp::getUid).collect(Collectors.toList());
		
		return null;
	}
	
	private void checkRecall(Long uid, Message message) {
        AssertUtil.isNotEmpty(message, "消息有误");
        AssertUtil.notEqual(message.getType(), MessageTypeEnum.RECALL.getType(),"消息无法撤回");
	    boolean hasPower = iRoleService.hasPower(uid, RoleEnum.CHAT_MANAGER);
	    if (hasPower) {
		    return;
	    }
	    boolean self = Objects.equals(uid, message.getFromUid());
		AssertUtil.isTrue(self, "无权限撤回他人消息");
	    long between = DateUtil.between(message.getCreateTime(), new Date(), DateUnit.MINUTE);
	    AssertUtil.isTrue(between<2,"消息已超过可撤回时间");
	    
	    
    }
    
    private void check(ChatMessageReq request, Long uid) {
		Room room = roomCache.get(request.getRoomId());
		if (room.isHotRoom()) {//全员群跳过校验
			return;
		}
		if (room.isRoomFriend()) {
			RoomFriend roomFriend = roomFriendDao.getByRoomId(request.getRoomId());
			AssertUtil.equal(NormalOrNoEnum.NORMAL.getStatus(), roomFriend.getStatus(), "您已被对方拉黑");
			AssertUtil.isTrue(uid.equals(roomFriend.getUid1()) || uid.equals(roomFriend.getUid2()), "您已被对方拉黑");
		}
		if (room.isRoomGroup()) {
			RoomGroup roomGroup = roomGroupCache.get(request.getRoomId());
			GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
			AssertUtil.isNotEmpty(member, "您已被移除群聊");
		}
	}
	
	/**
	 * @param roomId
	 * @param receiveUid
	 * @return {@code Long}
	 */
	private Long getLastMsgId(Long roomId, Long receiveUid) {
		Room room = roomCache.get(roomId);
		AssertUtil.isNotEmpty(room, "房间号有误");
		if (room.isHotRoom()) {
			return null;
		}
		AssertUtil.isNotEmpty(receiveUid, "请先登录");
		Contact contact = contactDao.get(receiveUid, roomId);
		return contact.getLastMsgId();
	}
}
