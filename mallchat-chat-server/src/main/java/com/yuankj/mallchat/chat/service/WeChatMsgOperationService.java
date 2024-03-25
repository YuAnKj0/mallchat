package com.yuankj.mallchat.chat.service;

import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-25/9:17
 * @apiNote
 */

public interface WeChatMsgOperationService {
	/**
	 * 向被at的用户微信推送群聊消息
	 * @param senderUid
	 * @param receiverUidList
	 * @param msg
	 */
	void publishChatMsgToWeChatUser(long senderUid, List<Long> receiverUidList, String msg);
}
