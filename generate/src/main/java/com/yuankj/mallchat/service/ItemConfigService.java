package com.yuankj.mallchat.service;

import com.yuankj.mallchat.model.ItemConfig;
    /**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface ItemConfigService{


    int deleteByPrimaryKey(Long id);

    int insert(ItemConfig record);

    int insertSelective(ItemConfig record);

    ItemConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ItemConfig record);

    int updateByPrimaryKey(ItemConfig record);

}
