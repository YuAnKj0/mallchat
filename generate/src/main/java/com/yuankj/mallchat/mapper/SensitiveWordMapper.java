package com.yuankj.mallchat.mapper;

import com.yuankj.mallchat.model.SensitiveWord;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface SensitiveWordMapper {
    int insert(SensitiveWord record);

    int insertSelective(SensitiveWord record);
}