package com.yuankj.mallchat.user.service.adapter;

import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.user.domain.enums.WSBaseResp;
import com.yuankj.mallchat.user.domain.enums.WSRespTypeEnum;
import com.yuankj.mallchat.user.domain.vo.response.ws.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ykj
 * @date 2024-03-22/20:02
 * @apiNote ws消息适配器
 */
@Component
public class WSAdapter {
	@Autowired
	private ChatService chatService;
	
	public static WSBaseResp<ChatMessageResp> buildMsgSend(ChatMessageResp msgResp) {
		WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
		wsBaseResp.setType(WSRespTypeEnum.MESSAGE.getType());
		wsBaseResp.setData(msgResp);
		return wsBaseResp;
	}
	
	public static WSBaseResp<WSLoginUrl> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
		WSBaseResp<WSLoginUrl> wsBaseResp = new WSBaseResp<>();
		wsBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
		wsBaseResp.setData(WSLoginUrl.builder().loginUrl(wxMpQrCodeTicket.getUrl()).build());
		return wsBaseResp;
	}
}
