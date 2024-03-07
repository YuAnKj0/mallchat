package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.MessageMark;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface MessageMarkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageMark record);

    int insertSelective(MessageMark record);

    MessageMark selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageMark record);

    int updateByPrimaryKey(MessageMark record);
}