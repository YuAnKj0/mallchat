package com.yuankj.mallchat.chatai.handler;

import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chatai.properties.ChatGPTProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ykj
 * @date 2024-03-25/10:19
 * @apiNote
 */
@Slf4j
@Component
public class GPTChatAIHandler extends AbstractChatAIHandler{
	@Autowired
	private ChatGPTProperties chatGPTProperties;
	/**
	 * @param message 
	 * @return
	 */
	@Override
	protected boolean supports(Message message) {
		if (!chatGPTProperties.isUse()) {
			return false;
		}
	}
	
	/**
	 * @param message 
	 * @return
	 */
	@Override
	protected String doChat(Message message) {
		return null;
	}
	
	/**
	 * @return 
	 */
	@Override
	public Long getChatAIUserId() {
		return null;
	}
}
