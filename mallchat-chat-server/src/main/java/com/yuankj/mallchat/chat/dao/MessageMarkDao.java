package com.yuankj.mallchat.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.chat.domain.entity.MessageMark;
import com.yuankj.mallchat.chat.mapper.MessageMarkMapper;
import com.yuankj.mallchat.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ykj
 * @date 2024/02/26
 * @apiNote  消息标记表 服务实现类
 */
@Service
public class MessageMarkDao extends ServiceImpl<MessageMarkMapper, MessageMark> {
    
    public MessageMark get(Long uid, Long msgId, Integer markType) {
        return lambdaQuery().eq(MessageMark::getUid, uid)
                .eq(MessageMark::getMsgId, msgId)
                .eq(MessageMark::getType, markType)
                .one();
    }
    
    public Integer getMarkCount(Long msgId, Integer markType) {
        return lambdaQuery().eq(MessageMark::getMsgId, msgId)
                .eq(MessageMark::getType, markType)
                .eq(MessageMark::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .count();
    }
    
    public List<MessageMark> getValidMarkByMsgIdBatch(List<Long> msgIds) {
        return lambdaQuery()
                .in(MessageMark::getMsgId, msgIds)
                .eq(MessageMark::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .list();
    }
}
