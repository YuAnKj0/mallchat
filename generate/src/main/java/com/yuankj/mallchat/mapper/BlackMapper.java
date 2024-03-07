package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.Black;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:14
 * @apiNote 
 */
 
  
public interface BlackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Black record);

    int insertSelective(Black record);

    Black selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Black record);

    int updateByPrimaryKey(Black record);
}