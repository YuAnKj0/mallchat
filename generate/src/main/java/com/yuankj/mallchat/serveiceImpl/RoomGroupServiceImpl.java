package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.model.RoomGroup;
import com.yuankj.mallchat.mapper.RoomGroupMapper;
import com.yuankj.mallchat.service.RoomGroupService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class RoomGroupServiceImpl implements RoomGroupService{

    @Resource
    private RoomGroupMapper roomGroupMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return roomGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(RoomGroup record) {
        return roomGroupMapper.insert(record);
    }

    @Override
    public int insertSelective(RoomGroup record) {
        return roomGroupMapper.insertSelective(record);
    }

    @Override
    public RoomGroup selectByPrimaryKey(Long id) {
        return roomGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RoomGroup record) {
        return roomGroupMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RoomGroup record) {
        return roomGroupMapper.updateByPrimaryKey(record);
    }

}
