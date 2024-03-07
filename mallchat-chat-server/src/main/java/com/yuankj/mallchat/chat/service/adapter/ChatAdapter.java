package com.yuankj.mallchat.chat.service.adapter;

import com.yuankj.mallchat.chat.domain.entity.Room;
import com.yuankj.mallchat.chat.domain.entity.RoomFriend;
import com.yuankj.mallchat.chat.domain.enums.HotFlagEnum;
import com.yuankj.mallchat.chat.domain.enums.RoomTypeEnum;
import com.yuankj.mallchat.common.domain.enums.NormalOrNoEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-03-07/9:53
 * @apiNote
 */

public class ChatAdapter {
	public static final String SEPARATOR = ",";
	
	public static String generateRoomKey(List<Long> list) {
		return list.stream().sorted().map(String::valueOf).collect(Collectors.joining(SEPARATOR));
	}
	
	public static Room buildRoom(RoomTypeEnum typeEnum) {
		Room room = new Room();
		room.setType(typeEnum.getType());
		room.setHotFlag(HotFlagEnum.NOT.getType());
		return room;
	}
	
	public static RoomFriend buildFriendRoom(Long roomId, List<Long> uidList) {
		List<Long> collect = uidList.stream().sorted().collect(Collectors.toList());
		RoomFriend roomFriend = new RoomFriend();
		roomFriend.setRoomId(roomId);
		roomFriend.setUid1(collect.get(0));
		roomFriend.setUid2(collect.get(1));
		roomFriend.setRoomKey(generateRoomKey(uidList));
		roomFriend.setStatus(NormalOrNoEnum.NORMAL.getStatus());
		return roomFriend;
	}
}
