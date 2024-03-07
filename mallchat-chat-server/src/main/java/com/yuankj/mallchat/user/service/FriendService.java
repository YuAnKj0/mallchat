package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.request.PageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.domain.vo.response.PageBaseResp;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendApplyReq;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendApproveReq;
import com.yuankj.mallchat.user.domain.vo.request.friend.FriendCheckReq;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendApplyResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendCheckResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendResp;
import com.yuankj.mallchat.user.domain.vo.response.friend.FriendUnreadResp;

/**
 * @author Ykj
 * @date 2024-03-06/15:39
 * @apiNote
 */

public interface FriendService {
	FriendCheckResp check(Long uid, FriendCheckReq request);
	
	void apply(Long uid, FriendApplyReq request);
	
	void applyApprove(Long uid, FriendApproveReq friendApproveReq);
	
	void deleteFriend(Long uid, Long targetUid);
	
	PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request);
	
	FriendUnreadResp unread(Long uid);
	
	CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);
}
