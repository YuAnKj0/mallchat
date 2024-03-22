package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.vo.request.chat.ChatMessageMemberReq;
import com.yuankj.mallchat.chat.domain.vo.request.chat.GroupAddReq;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberDelReq;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberReq;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMemberListResp;
import com.yuankj.mallchat.chat.domain.vo.response.ChatRoomResp;
import com.yuankj.mallchat.chat.domain.vo.response.MemberResp;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.user.domain.vo.response.ws.ChatMemberResp;

import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-07/11:11
 * @apiNote
 */

public interface RoomAppService {
	CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq request, Long uid);
	
	ChatRoomResp getContactDetail(Long uid, long id);
	
	ChatRoomResp getContactDetailByFriend(Long uid, Long uid1);
	
	MemberResp getGroupDetail(Long uid, long id);
	
	CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq request);
	
	List<ChatMemberListResp> getMemberList(ChatMessageMemberReq request);
	
	void delMember(Long uid, MemberDelReq request);
	
	Long addGroup(Long uid, GroupAddReq request);
}
