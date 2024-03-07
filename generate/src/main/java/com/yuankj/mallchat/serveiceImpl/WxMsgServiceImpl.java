package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.model.WxMsg;
import com.yuankj.mallchat.mapper.WxMsgMapper;
import com.yuankj.mallchat.service.WxMsgService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class WxMsgServiceImpl implements WxMsgService{

    @Resource
    private WxMsgMapper wxMsgMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wxMsgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WxMsg record) {
        return wxMsgMapper.insert(record);
    }

    @Override
    public int insertSelective(WxMsg record) {
        return wxMsgMapper.insertSelective(record);
    }

    @Override
    public WxMsg selectByPrimaryKey(Long id) {
        return wxMsgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WxMsg record) {
        return wxMsgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WxMsg record) {
        return wxMsgMapper.updateByPrimaryKey(record);
    }

}
