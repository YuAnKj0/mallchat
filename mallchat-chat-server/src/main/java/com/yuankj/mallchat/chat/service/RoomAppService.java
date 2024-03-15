package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.vo.response.ChatRoomResp;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;

/**
 * @author Ykj
 * @date 2024-03-07/11:11
 * @apiNote
 */

public interface RoomAppService {
	CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq request, Long uid);
	
	ChatRoomResp getContactDetail(Long uid, long id);
	
	ChatRoomResp getContactDetailByFriend(Long uid, Long uid1);
}
