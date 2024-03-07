package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.model.RoomFriend;
import com.yuankj.mallchat.mapper.RoomFriendMapper;
import com.yuankj.mallchat.service.RoomFriendService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class RoomFriendServiceImpl implements RoomFriendService{

    @Resource
    private RoomFriendMapper roomFriendMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return roomFriendMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(RoomFriend record) {
        return roomFriendMapper.insert(record);
    }

    @Override
    public int insertSelective(RoomFriend record) {
        return roomFriendMapper.insertSelective(record);
    }

    @Override
    public RoomFriend selectByPrimaryKey(Long id) {
        return roomFriendMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RoomFriend record) {
        return roomFriendMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RoomFriend record) {
        return roomFriendMapper.updateByPrimaryKey(record);
    }

}
