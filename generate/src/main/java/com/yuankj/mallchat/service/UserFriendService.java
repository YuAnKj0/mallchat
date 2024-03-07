package com.yuankj.mallchat.service;

import com.yuankj.mallchat.model.UserFriend;
    /**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface UserFriendService{


    int deleteByPrimaryKey(Long id);

    int insert(UserFriend record);

    int insertSelective(UserFriend record);

    UserFriend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFriend record);

    int updateByPrimaryKey(UserFriend record);

}
