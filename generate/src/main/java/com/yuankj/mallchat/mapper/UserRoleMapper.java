package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.UserRole;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface UserRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}