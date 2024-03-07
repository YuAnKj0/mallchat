package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.UserApply;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface UserApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserApply record);

    int insertSelective(UserApply record);

    UserApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserApply record);

    int updateByPrimaryKey(UserApply record);
}