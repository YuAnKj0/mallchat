package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.RoomFriend;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface RoomFriendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomFriend record);

    int insertSelective(RoomFriend record);

    RoomFriend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomFriend record);

    int updateByPrimaryKey(RoomFriend record);
}