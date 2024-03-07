package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.ContactMapper;
import com.yuankj.mallchat.model.Contact;
import com.yuankj.mallchat.service.ContactService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class ContactServiceImpl implements ContactService{

    @Resource
    private ContactMapper contactMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return contactMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Contact record) {
        return contactMapper.insert(record);
    }

    @Override
    public int insertSelective(Contact record) {
        return contactMapper.insertSelective(record);
    }

    @Override
    public Contact selectByPrimaryKey(Long id) {
        return contactMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Contact record) {
        return contactMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Contact record) {
        return contactMapper.updateByPrimaryKey(record);
    }

}
