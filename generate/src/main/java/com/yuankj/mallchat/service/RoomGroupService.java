package com.yuankj.mallchat.service;

import com.yuankj.mallchat.model.RoomGroup;
    /**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface RoomGroupService{


    int deleteByPrimaryKey(Long id);

    int insert(RoomGroup record);

    int insertSelective(RoomGroup record);

    RoomGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomGroup record);

    int updateByPrimaryKey(RoomGroup record);

}
