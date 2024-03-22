package com.yuankj.mallchat.user.service.adapter;

import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.user.domain.enums.WSBaseResp;
import com.yuankj.mallchat.user.domain.enums.WSRespTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author Ykj
 * @date 2024-03-22/20:02
 * @apiNote ws消息适配器
 */
@Component

public class WSAdapter {
	
	
	public static WSBaseResp<ChatMessageResp> buildMsgSend(ChatMessageResp msgResp) {
		WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
		wsBaseResp.setType(WSRespTypeEnum.MESSAGE.getType());
		wsBaseResp.setData(msgResp);
		return wsBaseResp;
	}
}
