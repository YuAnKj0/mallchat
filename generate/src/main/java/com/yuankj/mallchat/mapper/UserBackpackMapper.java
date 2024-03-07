package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.UserBackpack;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface UserBackpackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBackpack record);

    int insertSelective(UserBackpack record);

    UserBackpack selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBackpack record);

    int updateByPrimaryKey(UserBackpack record);
}