package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.entity.RoomFriend;
import com.yuankj.mallchat.chat.domain.entity.RoomGroup;

import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-07/9:48
 * @apiNote
 */

public interface RoomService {
	RoomFriend createFriendRoom(List<Long> list);
	
	void disableFriendRoom(List<Long> list);
	
	RoomFriend getFriendRoom(Long uid, Long friendUid);
	
	RoomGroup createGroupRoom(Long uid);
}
