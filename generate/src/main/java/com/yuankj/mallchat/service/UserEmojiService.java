package com.yuankj.mallchat.service;

import com.yuankj.mallchat.model.UserEmoji;
    /**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface UserEmojiService{


    int deleteByPrimaryKey(Long id);

    int insert(UserEmoji record);

    int insertSelective(UserEmoji record);

    UserEmoji selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserEmoji record);

    int updateByPrimaryKey(UserEmoji record);

}
