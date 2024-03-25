package com.yuankj.mallchat.chatai.handler;

import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.enums.MessageTypeEnum;
import com.yuankj.mallchat.chat.domain.vo.request.chat.ChatMessageReq;
import com.yuankj.mallchat.chat.domain.vo.request.msg.TextMsgReq;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.common.config.ThreadPoolConfig;
import com.yuankj.mallchat.user.domain.vo.response.user.UserInfoResp;
import com.yuankj.mallchat.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;

/**
 * @author Ykj
 * @date 2024-03-25/10:12
 * @apiNote
 */
@Slf4j
public abstract class AbstractChatAIHandler {
	@Autowired
	@Qualifier(ThreadPoolConfig.AICHAT_EXECUTOR)
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	protected ChatService chatService;
	@Autowired
	protected UserService userService;
	
	
	public void chat(Message message) {
		if (!supports(message)) {
			return;
		}
		threadPoolTaskExecutor.execute(() -> {
			String text = doChat(message);
			if (StringUtils.isNotBlank(text)) {
				answerMsg(text, message);
			}
		});
	}
	protected abstract boolean supports(Message message);
	protected abstract String doChat(Message message);
	public abstract Long getChatAIUserId();
	protected  void answerMsg(String text, Message message){
		UserInfoResp userInfo = userService.getUserInfo(message.getFromUid());
		text="@"+userInfo.getName()+" "+text;
		if (text.length()<800){
			save(text,message);
		}else {
			int maxLen = 800;
			int len = text.length();
			int count = (len + maxLen - 1) / maxLen;
			
			for (int i = 0; i < count; i++) {
				int start = i * maxLen;
				int end = Math.min(start + maxLen, len);
				save(text.substring(start, end), message);
			}
		}
	}
	
	private void save(String text, Message replyMessage) {
		Long roomId = replyMessage.getRoomId();
		Long uid = replyMessage.getFromUid();
		Long id = replyMessage.getId();
		ChatMessageReq answerReq = new ChatMessageReq();
		answerReq.setRoomId(roomId);
		answerReq.setMsgType(MessageTypeEnum.TEXT.getType());
		TextMsgReq textMsgReq = new TextMsgReq();
		textMsgReq.setContent(text);
		textMsgReq.setReplyMsgId(replyMessage.getId());
		textMsgReq.setAtUidList(Collections.singletonList(uid));
		answerReq.setBody(textMsgReq);
		chatService.sendMsg(answerReq, getChatAIUserId());
	}
}
