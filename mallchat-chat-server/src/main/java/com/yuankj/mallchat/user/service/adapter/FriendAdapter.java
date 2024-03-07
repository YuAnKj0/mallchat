package com.yuankj.mallchat.user.service.adapter;

import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.entity.UserApply;
import com.yuankj.mallchat.user.domain.entity.UserFriend;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendApplyReq;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendApplyResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendResp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yuankj.mallchat.user.domain.enums.ApplyReadStatusEnum.UNREAD;
import static com.yuankj.mallchat.user.domain.enums.ApplyStatusEnum.WAIT_APPROVAL;
import static com.yuankj.mallchat.user.domain.enums.ApplyTypeEnum.ADD_FRIEND;

/**
 * @author Ykj
 * @date 2024-03-07/9:25
 * @apiNote 好友适配器
 */

public class FriendAdapter {
	
	public static UserApply buildFriendApply(Long uid, FriendApplyReq request){
		UserApply userApply = new UserApply();
		userApply.setUid(uid);
		userApply.setMsg(request.getMsg());
		userApply.setType(ADD_FRIEND.getCode());
		userApply.setTargetId(request.getTargetUid());
		userApply.setStatus(WAIT_APPROVAL.getCode());
		userApply.setReadStatus(UNREAD.getCode());
		return userApply;
	}
	
	public static List<FriendApplyResp> buildFriendApplyList(List<UserApply> records) {
		return records.stream().map(userApply -> {
			FriendApplyResp friendApplyResp = new FriendApplyResp();
			friendApplyResp.setUid(userApply.getUid());
			friendApplyResp.setType(userApply.getType());
			friendApplyResp.setApplyId(userApply.getId());
			friendApplyResp.setMsg(userApply.getMsg());
			friendApplyResp.setStatus(userApply.getStatus());
			return friendApplyResp;
		}).collect(Collectors.toList());
	}
	public static List<FriendResp> buildFriend(List<UserFriend> list, List<User> userList) {
		Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
		return list.stream().map(userFriend -> {
			FriendResp resp = new FriendResp();
			resp.setUid(userFriend.getFriendUid());
			User user = userMap.get(userFriend.getFriendUid());
			if (Objects.nonNull(user)) {
				resp.setActiveStatus(user.getActiveStatus());
			}
			return resp;
		}).collect(Collectors.toList());
	}
	
}
