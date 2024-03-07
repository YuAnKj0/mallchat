package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.RoomMapper;
import com.yuankj.mallchat.model.Room;
import com.yuankj.mallchat.service.RoomService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class RoomServiceImpl implements RoomService{

    @Resource
    private RoomMapper roomMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return roomMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Room record) {
        return roomMapper.insert(record);
    }

    @Override
    public int insertSelective(Room record) {
        return roomMapper.insertSelective(record);
    }

    @Override
    public Room selectByPrimaryKey(Long id) {
        return roomMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Room record) {
        return roomMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Room record) {
        return roomMapper.updateByPrimaryKey(record);
    }

}
