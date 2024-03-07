package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.UserApplyMapper;
import com.yuankj.mallchat.model.UserApply;
import com.yuankj.mallchat.service.UserApplyService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class UserApplyServiceImpl implements UserApplyService{

    @Resource
    private UserApplyMapper userApplyMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userApplyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserApply record) {
        return userApplyMapper.insert(record);
    }

    @Override
    public int insertSelective(UserApply record) {
        return userApplyMapper.insertSelective(record);
    }

    @Override
    public UserApply selectByPrimaryKey(Long id) {
        return userApplyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserApply record) {
        return userApplyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserApply record) {
        return userApplyMapper.updateByPrimaryKey(record);
    }

}
