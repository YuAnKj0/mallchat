package com.yuankj.mallchat.user.service.handler;

import com.yuankj.mallchat.user.service.WxMsgService;
import com.yuankj.mallchat.user.service.adapter.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ykj
 * @date 2024-03-25/15:05
 * @apiNote
 */
@Component
public class SubscribeHandler extends AbstractHandler{
	@Autowired
	private WxMsgService wxMsgService;
	/**
	 * @param wxMessage 
	 * @param context
	 * @param weixinService
	 * @param sessionManager
	 * @return
	 * @throws WxErrorException
	 */
	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
	                                Map<String, Object> context, WxMpService weixinService,
	                                WxSessionManager sessionManager) throws WxErrorException {
		this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());
		WxMpXmlOutMessage responseResult = null;
		try {
			responseResult=this.handleSpecial(weixinService,wxMessage);
		} catch (Exception e) {
			//错误处理
			this.logger.error(e.getMessage(), e);
		}
		if (responseResult != null) {
			return responseResult;
		}
		
		try {
			return new TextBuilder().build("感谢关注", wxMessage, weixinService);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private WxMpXmlOutMessage handleSpecial(WxMpService weixinService, WxMpXmlMessage wxMessage) {
		return wxMsgService.scan(weixinService, wxMessage);
	}
}
