package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.model.MessageMark;
import com.yuankj.mallchat.mapper.MessageMarkMapper;
import com.yuankj.mallchat.service.MessageMarkService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class MessageMarkServiceImpl implements MessageMarkService{

    @Resource
    private MessageMarkMapper messageMarkMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return messageMarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(MessageMark record) {
        return messageMarkMapper.insert(record);
    }

    @Override
    public int insertSelective(MessageMark record) {
        return messageMarkMapper.insertSelective(record);
    }

    @Override
    public MessageMark selectByPrimaryKey(Long id) {
        return messageMarkMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(MessageMark record) {
        return messageMarkMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MessageMark record) {
        return messageMarkMapper.updateByPrimaryKey(record);
    }

}
