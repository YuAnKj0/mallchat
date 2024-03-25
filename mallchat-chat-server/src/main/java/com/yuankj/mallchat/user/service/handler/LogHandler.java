package com.yuankj.mallchat.user.service.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ykj
 * @date 2024-03-25/14:50
 * @apiNote
 */
@Component
@Slf4j
public class LogHandler extends AbstractHandler{
	/**
	 * @param wxMpXmlMessage 
	 * @param map
	 * @param wxMpService
	 * @param wxSessionManager
	 * @return
	 * @throws WxErrorException
	 */
	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
		log.info("\n接收到请求消息，内容：{}", JSONUtil.toJsonStr(wxMpXmlMessage));
		return null;
	}
}
