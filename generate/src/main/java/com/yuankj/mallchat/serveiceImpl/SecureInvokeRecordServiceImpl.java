package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.SecureInvokeRecordMapper;
import com.yuankj.mallchat.model.SecureInvokeRecord;
import com.yuankj.mallchat.service.SecureInvokeRecordService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class SecureInvokeRecordServiceImpl implements SecureInvokeRecordService{

    @Resource
    private SecureInvokeRecordMapper secureInvokeRecordMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return secureInvokeRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SecureInvokeRecord record) {
        return secureInvokeRecordMapper.insert(record);
    }

    @Override
    public int insertSelective(SecureInvokeRecord record) {
        return secureInvokeRecordMapper.insertSelective(record);
    }

    @Override
    public SecureInvokeRecord selectByPrimaryKey(Long id) {
        return secureInvokeRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SecureInvokeRecord record) {
        return secureInvokeRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SecureInvokeRecord record) {
        return secureInvokeRecordMapper.updateByPrimaryKey(record);
    }

}
