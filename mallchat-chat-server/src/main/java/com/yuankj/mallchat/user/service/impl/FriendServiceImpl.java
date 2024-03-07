package com.yuankj.mallchat.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.yuankj.mallchat.chat.domain.entity.RoomFriend;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.chat.service.RoomService;
import com.yuankj.mallchat.chat.service.adapter.MessageAdapter;
import com.yuankj.mallchat.common.annocation.RedissonLock;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.request.PageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.domain.vo.response.PageBaseResp;
import com.yuankj.mallchat.common.event.UserApplyEvent;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.user.dao.UserDao;
import com.yuankj.mallchat.user.domain.dao.UserApplyDao;
import com.yuankj.mallchat.user.domain.dao.UserFriendDao;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.entity.UserApply;
import com.yuankj.mallchat.user.domain.entity.UserFriend;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendApplyReq;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendApproveReq;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendCheckReq;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendApplyResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendCheckResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendUnreadResp;
import com.yuankj.mallchat.user.service.FriendService;
import com.yuankj.mallchat.user.service.adapter.FriendAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.yuankj.mallchat.user.domain.enums.ApplyStatusEnum.WAIT_APPROVAL;

/**
 * @author Ykj
 * @date 2024-03-06/15:39
 * @apiNote
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {
	
	@Autowired
	private UserFriendDao userFriendDao;
	@Autowired
	private UserApplyDao userApplyDao;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private RoomService roomService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private UserDao userDao;
	
	/**
	 * @param uid
	 * @param request
	 * @return
	 */
	@Override
	public FriendCheckResp check(Long uid, FriendCheckReq request) {
		List<UserFriend> friendList = userFriendDao.getByFriends(uid, request.getUidList());
		Set<Long> friendUidList = friendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
		List<FriendCheckResp.FriendCheck> friendCheckList = request.getUidList().stream().map(friendUid -> {
			FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
			friendCheck.setUid(friendUid);
			friendCheck.setIsFriend(friendUidList.contains(friendUid));
			return friendCheck;
		}).collect(Collectors.toList());
		return new FriendCheckResp(friendCheckList);
	}
	
	/**
	 * 申请好友
	 *
	 * @param uid
	 * @param request
	 */
	@Override
	@RedissonLock(key = "#uid")
	public void apply(Long uid, FriendApplyReq request) {
		UserFriend friend = userFriendDao.getByFriend(uid, request.getTargetUid());
		AssertUtil.isEmpty(friend, "已经是好友了");
		//是否有带申请的好友记录(自己的)
		UserApply selfApproving = userApplyDao.getFriendApproving(uid, request.getTargetUid());
		if (Objects.nonNull(selfApproving)) {
			log.info("已有好友申请记录,uid:{}, targetId:{}", uid, request.getTargetUid());
			return;
		}
		//是否有待审批的申请记录(别人请求自己的)
		UserApply friendApproving = userApplyDao.getFriendApproving(request.getTargetUid(), uid);
		if (Objects.nonNull(friendApproving)) {
			((FriendService) AopContext.currentProxy()).applyApprove(uid, new FriendApproveReq(friendApproving.getId()));
			return;
		}
		//申请入库
		UserApply userApply = FriendAdapter.buildFriendApply(uid, request);
		userApplyDao.save(userApply);
		//推送消息
		applicationEventPublisher.publishEvent(new UserApplyEvent(this, userApply));
		
	}
	
	/**
	 * @param uid
	 * @param request
	 */
	@Override
	public void applyApprove(Long uid, FriendApproveReq request) {
		UserApply userApply = userApplyDao.getById(request.getApplyId());
		AssertUtil.isNotEmpty(userApply, "不存在申请记录");
		AssertUtil.equal(userApply.getTargetId(), uid, "不存在申请记录");
		AssertUtil.equal(userApply.getStatus(), WAIT_APPROVAL.getCode(), "已同意好友申请");
		//同意申请
		userApplyDao.agree(request.getApplyId());
		//创建双方好友关系
		createFriend(uid, userApply.getUid());
		//创建一个聊天房间
		RoomFriend roomFriend = roomService.createFriendRoom(Arrays.asList(uid, userApply.getUid()));
		//发送一条同意消息。。我们已经是好友了，开始聊天吧
		chatService.sendMsg(MessageAdapter.buildAgreeMsg(roomFriend.getRoomId()), uid);
	}
	
	/**
	 * @param uid 
	 * @param targetUid
	 */
	@Override
	public void deleteFriend(Long uid, Long targetUid) {
		List<UserFriend> userFriends = userFriendDao.getUserFriend(uid, targetUid);
		if (CollectionUtil.isEmpty(userFriends)) {
			log.info("没有好友关系：{},{}", uid, targetUid);
			return;
		}
		List<Long> friendRecordIds = userFriends.stream().map(UserFriend::getId).collect(Collectors.toList());
		userFriendDao.removeByIds(friendRecordIds);
		//禁用房间
		roomService.disableFriendRoom(Arrays.asList(uid, targetUid));
	}
	
	/**
	 * @param uid 
	 * @param request
	 * @return
	 */
	@Override
	public PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request) {
		IPage<UserApply> userApplyIPage = userApplyDao.friendApplyPage(uid, request.plusPage());
		if (CollectionUtil.isEmpty(userApplyIPage.getRecords())) {
			return PageBaseResp.empty();
		}
		//将这些申请列表设为已读
		readApples(uid, userApplyIPage);
		//返回消息
		return PageBaseResp.init(userApplyIPage, FriendAdapter.buildFriendApplyList(userApplyIPage.getRecords()));
	}
	
	/**
	 * @param uid 
	 * @return
	 */
	@Override
	public FriendUnreadResp unread(Long uid) {
		Integer unReadCount = userApplyDao.getUnReadCount(uid);
		return new FriendUnreadResp(unReadCount);
	}
	
	/**
	 * @param uid 
	 * @param request
	 * @return
	 */
	@Override
	public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
		CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, request);
		if (CollectionUtil.isEmpty(friendPage.getList())) {
			return CursorPageBaseResp.empty();
		}
		List<Long> friendUids = friendPage.getList().stream().map(UserFriend::getFriendUid).collect(Collectors.toList());
		List<User> userList =userDao.getFriendList(friendUids);
		return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriend(friendPage.getList(), userList));
	}
	
	private void readApples(Long uid, IPage<UserApply> userApplyIPage) {
		List<Long> applyIds = userApplyIPage.getRecords().stream().map(UserApply::getId).collect(Collectors.toList());
		userApplyDao.readApples(uid, applyIds);
	}
	
	
	private void createFriend(Long uid, Long targetUid) {
		UserFriend userFriend1 = new UserFriend();
		userFriend1.setUid(uid);
		userFriend1.setFriendUid(targetUid);
		UserFriend userFriend2 = new UserFriend();
		userFriend2.setUid(targetUid);
		userFriend2.setFriendUid(uid);
		userFriendDao.saveBatch(Lists.newArrayList(userFriend1, userFriend2));
	}
}
