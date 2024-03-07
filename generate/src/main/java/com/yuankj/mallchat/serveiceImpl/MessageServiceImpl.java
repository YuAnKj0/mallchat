package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.model.Message;
import com.yuankj.mallchat.mapper.MessageMapper;
import com.yuankj.mallchat.service.MessageService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class MessageServiceImpl implements MessageService{

    @Resource
    private MessageMapper messageMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Message record) {
        return messageMapper.insert(record);
    }

    @Override
    public int insertSelective(Message record) {
        return messageMapper.insertSelective(record);
    }

    @Override
    public Message selectByPrimaryKey(Long id) {
        return messageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Message record) {
        return messageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Message record) {
        return messageMapper.updateByPrimaryKey(record);
    }

}
