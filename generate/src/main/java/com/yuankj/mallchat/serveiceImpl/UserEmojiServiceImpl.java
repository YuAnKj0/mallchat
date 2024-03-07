package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.UserEmojiMapper;
import com.yuankj.mallchat.model.UserEmoji;
import com.yuankj.mallchat.service.UserEmojiService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class UserEmojiServiceImpl implements UserEmojiService{

    @Resource
    private UserEmojiMapper userEmojiMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userEmojiMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserEmoji record) {
        return userEmojiMapper.insert(record);
    }

    @Override
    public int insertSelective(UserEmoji record) {
        return userEmojiMapper.insertSelective(record);
    }

    @Override
    public UserEmoji selectByPrimaryKey(Long id) {
        return userEmojiMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserEmoji record) {
        return userEmojiMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserEmoji record) {
        return userEmojiMapper.updateByPrimaryKey(record);
    }

}
