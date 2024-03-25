package com.yuankj.mallchat.chatai.service.impl;

import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.entity.msg.MessageExtra;
import com.yuankj.mallchat.chatai.handler.AbstractChatAIHandler;
import com.yuankj.mallchat.chatai.handler.ChatAIHandlerFactory;
import com.yuankj.mallchat.chatai.service.IChatAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Ykj
 * @date 2024-03-25/10:10
 * @apiNote
 */
@Slf4j
@Service
public class ChatAIServiceImpl implements IChatAIService {
	/**
	 * @param message 
	 */
	@Override
	public void chat(Message message) {
		MessageExtra extra=message.getExtra();
		if (extra==null) {
			return;
		}
		AbstractChatAIHandler chatAI= ChatAIHandlerFactory.getChatAIHandlerById(extra.getAtUidList());
		if (chatAI != null) {
			chatAI.chat(message);
		}
	}
}
