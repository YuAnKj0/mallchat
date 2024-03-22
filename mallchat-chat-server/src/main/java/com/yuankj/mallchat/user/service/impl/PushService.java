package com.yuankj.mallchat.user.service.impl;

import com.yuankj.mallchat.common.constant.MQConstant;
import com.yuankj.mallchat.common.domain.dto.PushMessageDTO;
import com.yuankj.mallchat.service.MQProducer;
import com.yuankj.mallchat.user.domain.enums.WSBaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-22/17:24
 * @apiNote
 */
@Service
public class PushService {
	@Autowired
	private MQProducer mqProducer;
	public void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList){
		mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(uidList, msg));
	}
	public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
		mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(uid, msg));
	}
	
	public void sendPushMsg(WSBaseResp<?> msg) {
		mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg));
	}
	
}
