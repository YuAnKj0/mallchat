package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.WxMsg;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface WxMsgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WxMsg record);

    int insertSelective(WxMsg record);

    WxMsg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WxMsg record);

    int updateByPrimaryKey(WxMsg record);
}