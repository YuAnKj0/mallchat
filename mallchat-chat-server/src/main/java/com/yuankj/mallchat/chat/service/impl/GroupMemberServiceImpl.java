package com.yuankj.mallchat.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yuankj.mallchat.chat.dao.*;
import com.yuankj.mallchat.chat.domain.entity.Room;
import com.yuankj.mallchat.chat.domain.entity.RoomGroup;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberExitReq;
import com.yuankj.mallchat.chat.domain.vo.response.ws.WSMemberChange;
import com.yuankj.mallchat.chat.service.IGroupMemberService;
import com.yuankj.mallchat.chat.service.adapter.MemberAdapter;
import com.yuankj.mallchat.chat.service.cache.GroupMemberCache;
import com.yuankj.mallchat.common.exception.CommonErrorEnum;
import com.yuankj.mallchat.common.exception.GroupErrorEnum;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.user.domain.enums.WSBaseResp;
import com.yuankj.mallchat.user.service.impl.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-20/10:19
 * @apiNote
 */
@Service
public class GroupMemberServiceImpl implements IGroupMemberService{
	@Autowired
	private GroupMemberDao groupMemberDao;
	
	@Autowired
	private RoomGroupDao roomGroupDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private ContactDao contactDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private GroupMemberCache groupMemberCache;
	
	@Autowired
	private PushService pushService;
	/**
	 * @param uid 
	 * @param request
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void exitGroup(Long uid, MemberExitReq request) {
		Long roomId = request.getRoomId();
		//判断roomId是否存在
		RoomGroup roomGroup = roomGroupDao.getByRoomId(roomId);
		AssertUtil.isNotEmpty(roomGroup, GroupErrorEnum.GROUP_NOT_EXIST);
		// 2. 判断房间是否是大群聊 （大群聊禁止退出）
		Room room = roomDao.getById(roomId);
		AssertUtil.isFalse(room.isHotRoom(), GroupErrorEnum.NOT_ALLOWED_FOR_EXIT_GROUP);
		// 3. 判断群成员是否在群中
		Boolean isGroupShip = groupMemberDao.isGroupShip(roomGroup.getRoomId(), Collections.singletonList(uid));
		AssertUtil.isTrue(isGroupShip, GroupErrorEnum.USER_NOT_IN_GROUP);
		// 4. 判断该用户是否是群主
		Boolean isLord = groupMemberDao.isLord(roomGroup.getId(), uid);
		if (isLord) {
			// 4.1 删除房间
			boolean isDelRoom = roomDao.removeById(roomId);
			AssertUtil.isTrue(isDelRoom, CommonErrorEnum.SYSTEM_ERROR);
			// 4.2 删除会话
			Boolean isDelContact = contactDao.removeByRoomId(roomId, Collections.EMPTY_LIST);
			AssertUtil.isTrue(isDelContact, CommonErrorEnum.SYSTEM_ERROR);
			// 4.3 删除群成员
			Boolean isDelGroupMember = groupMemberDao.removeByGroupId(roomGroup.getId(), Collections.EMPTY_LIST);
			AssertUtil.isTrue(isDelGroupMember, CommonErrorEnum.SYSTEM_ERROR);
			// 4.4 删除消息记录 (逻辑删除)
			Boolean isDelMessage = messageDao.removeByRoomId(roomId, Collections.EMPTY_LIST);
			AssertUtil.isTrue(isDelMessage, CommonErrorEnum.SYSTEM_ERROR);
			// TODO 这里也可以告知群成员 群聊已被删除的消息
		}else {
			// 4.5 删除会话
			Boolean isDelContact = contactDao.removeByRoomId(roomId, Collections.singletonList(uid));
			AssertUtil.isTrue(isDelContact, CommonErrorEnum.SYSTEM_ERROR);
			// 4.6 删除群成员
			Boolean isDelGroupMember = groupMemberDao.removeByGroupId(roomGroup.getId(), Collections.singletonList(uid));
			AssertUtil.isTrue(isDelGroupMember, CommonErrorEnum.SYSTEM_ERROR);
			// 4.7 发送移除事件告知群成员
			List<Long> memberUidList = groupMemberCache.getMemberUidList(roomGroup.getRoomId());
			WSBaseResp<WSMemberChange> ws = MemberAdapter.buildMemberRemoveWS(roomGroup.getRoomId(), uid);
			pushService.sendPushMsg(ws,memberUidList);
			groupMemberCache.evictMemberUidList(room.getId());
		}
		
	}
}
