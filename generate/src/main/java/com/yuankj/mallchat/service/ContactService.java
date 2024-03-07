package com.yuankj.mallchat.service;

import com.yuankj.mallchat.model.Contact;
    /**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface ContactService{


    int deleteByPrimaryKey(Long id);

    int insert(Contact record);

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKey(Contact record);

}
