package com.yuankj.mallchat.chatai.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ykj
 * @date 2024-03-25/11:17
 * @apiNote
 */

public class ChatAIHandlerFactory {
	private static final Map<Long, AbstractChatAIHandler> CHATAI_ID_MAP = new ConcurrentHashMap<>();
	
	public static void register(Long aIUserId, AbstractChatAIHandler chatAIHandler) {
		CHATAI_ID_MAP.put(aIUserId, chatAIHandler);
	}
	
	
	public static AbstractChatAIHandler getChatAIHandlerById(List<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return null;
		}
		for (Long userId:userIds){
			AbstractChatAIHandler chatAIHandler = CHATAI_ID_MAP.get(userId);
			if (chatAIHandler!=null) {
				return chatAIHandler;
			}
		}
		return null;
	}
}
