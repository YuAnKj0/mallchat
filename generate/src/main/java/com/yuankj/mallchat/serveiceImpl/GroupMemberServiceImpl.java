package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.GroupMemberMapper;
import com.yuankj.mallchat.model.GroupMember;
import com.yuankj.mallchat.service.GroupMemberService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class GroupMemberServiceImpl implements GroupMemberService{

    @Resource
    private GroupMemberMapper groupMemberMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return groupMemberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GroupMember record) {
        return groupMemberMapper.insert(record);
    }

    @Override
    public int insertSelective(GroupMember record) {
        return groupMemberMapper.insertSelective(record);
    }

    @Override
    public GroupMember selectByPrimaryKey(Long id) {
        return groupMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(GroupMember record) {
        return groupMemberMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GroupMember record) {
        return groupMemberMapper.updateByPrimaryKey(record);
    }

}
