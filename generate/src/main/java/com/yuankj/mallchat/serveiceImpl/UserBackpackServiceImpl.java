package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.model.UserBackpack;
import com.yuankj.mallchat.mapper.UserBackpackMapper;
import com.yuankj.mallchat.service.UserBackpackService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class UserBackpackServiceImpl implements UserBackpackService{

    @Resource
    private UserBackpackMapper userBackpackMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userBackpackMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserBackpack record) {
        return userBackpackMapper.insert(record);
    }

    @Override
    public int insertSelective(UserBackpack record) {
        return userBackpackMapper.insertSelective(record);
    }

    @Override
    public UserBackpack selectByPrimaryKey(Long id) {
        return userBackpackMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserBackpack record) {
        return userBackpackMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserBackpack record) {
        return userBackpackMapper.updateByPrimaryKey(record);
    }

}
