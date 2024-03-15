package com.yuankj.mallchat.chat.service.impl;

import com.yuankj.mallchat.chat.dao.RoomDao;
import com.yuankj.mallchat.chat.dao.RoomFriendDao;
import com.yuankj.mallchat.chat.domain.entity.Room;
import com.yuankj.mallchat.chat.domain.entity.RoomFriend;
import com.yuankj.mallchat.chat.domain.enums.RoomTypeEnum;
import com.yuankj.mallchat.chat.service.RoomService;
import com.yuankj.mallchat.chat.service.adapter.ChatAdapter;
import com.yuankj.mallchat.common.domain.enums.NormalOrNoEnum;
import com.yuankj.mallchat.common.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Ykj
 * @date 2024-03-07/9:48
 * @apiNote
 */
@Service
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	private RoomFriendDao roomFriendDao;
	@Autowired
	private RoomDao roomDao;
	
	/**
	 * @param list
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RoomFriend createFriendRoom(List<Long> list) {
		AssertUtil.isNotEmpty(list, "房间创建失败，好友数量不对");
		AssertUtil.equal(list.size(), 2, "房间创建失败，好友数量不对");
		String key = ChatAdapter.generateRoomKey(list);
		RoomFriend roomFriend = roomFriendDao.getByKey(key);
		if (Objects.nonNull(roomFriend)) {
			restoreRoomIfNeed(roomFriend);
		} else {
			//新建房间
			Room room = createRoom(RoomTypeEnum.FRIEND);
			roomFriend = createFriendRoom(room.getId(), list);
		}
		return roomFriend;
	}
	
	/**
	 * @param list
	 */
	@Override
	public void disableFriendRoom(List<Long> list) {
		AssertUtil.isNotEmpty(list, "房间创建失败，好友数量不对");
		AssertUtil.equal(list.size(), 2, "房间创建失败，好友数量不对");
		String key = ChatAdapter.generateRoomKey(list);
		roomFriendDao.disableRoom(key);
		
	}
	
	/**
	 * @param uid 
	 * @param friendUid
	 * @return
	 */
	@Override
	public RoomFriend getFriendRoom(Long uid, Long friendUid) {
		String roomKey = ChatAdapter.generateRoomKey(Arrays.asList(uid, friendUid));
		return roomFriendDao.getByKey(roomKey);
	}
	
	private Room createRoom(RoomTypeEnum typeEnum) {
		Room insert = ChatAdapter.buildRoom(typeEnum);
		roomDao.save(insert);
		return insert;
	}
	
	private void restoreRoomIfNeed(RoomFriend roomFriend) {
		if (Objects.equals(roomFriend.getStatus(), NormalOrNoEnum.NOT_NORMAL.getStatus())) {
			roomFriendDao.restoreRoom(roomFriend.getId());
		}
	}
	
	private RoomFriend createFriendRoom(Long id, List<Long> uidList) {
		RoomFriend insert = ChatAdapter.buildFriendRoom(id, uidList);
		roomFriendDao.save(insert);
		return insert;
	}
}
