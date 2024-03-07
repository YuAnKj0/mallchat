package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.Role;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}