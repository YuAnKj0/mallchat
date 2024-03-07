package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.UserFriendMapper;
import com.yuankj.mallchat.model.UserFriend;
import com.yuankj.mallchat.service.UserFriendService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class UserFriendServiceImpl implements UserFriendService{

    @Resource
    private UserFriendMapper userFriendMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userFriendMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserFriend record) {
        return userFriendMapper.insert(record);
    }

    @Override
    public int insertSelective(UserFriend record) {
        return userFriendMapper.insertSelective(record);
    }

    @Override
    public UserFriend selectByPrimaryKey(Long id) {
        return userFriendMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserFriend record) {
        return userFriendMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserFriend record) {
        return userFriendMapper.updateByPrimaryKey(record);
    }

}
