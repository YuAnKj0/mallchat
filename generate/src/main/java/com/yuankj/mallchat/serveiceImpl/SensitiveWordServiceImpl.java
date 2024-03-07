package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.SensitiveWordMapper;
import com.yuankj.mallchat.model.SensitiveWord;
import com.yuankj.mallchat.service.SensitiveWordService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class SensitiveWordServiceImpl implements SensitiveWordService{

    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public int insert(SensitiveWord record) {
        return sensitiveWordMapper.insert(record);
    }

    @Override
    public int insertSelective(SensitiveWord record) {
        return sensitiveWordMapper.insertSelective(record);
    }

}
