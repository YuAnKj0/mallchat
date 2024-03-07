package com.yuankj.mallchat.user.controller;

import com.yuankj.mallchat.user.service.WxMsgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Ykj
 * @date 2024-02-27/10:00
 * @apiNote 微信api交互接口
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("wx/portal/public")
public class WxPortalController {
	
	private final WxMpService wxService;
	private final WxMsgService wxMsgService;
	private final WxMpMessageRouter messageRouter;
	
	@GetMapping(produces = "text/plain;charset=utf-8")
	public String authGet(@RequestParam(name = "signature", required = false) String signature,
	                      @RequestParam(name = "timestamp", required = false) String timestamp,
	                      @RequestParam(name = "nonce", required = false) String nonce,
	                      @RequestParam(name = "echostr", required = false) String echostr) {
		
		log.info("\n接收到来自微信服务器的消息:[{},{},{},{}]", signature, timestamp, nonce, echostr);
		if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
			throw new IllegalArgumentException("请求参数非法");
		}
		if (wxService.checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}
		return "非法请求";
		
	}
	
	@GetMapping("/callBack")
	public RedirectView callBack(@RequestParam String code) {
		try {
			WxOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
			WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, "zh_CN");
			wxMsgService.authorize(userInfo);
			
		} catch (WxErrorException e) {
			log.error("callBack error", e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://mp.weixin.qq.com/s/m,1SRsBG96kLJW5mPe4AVGA");
		return redirectView;
	}
	
	@PostMapping(produces = "application/xml; charset=UTF-8")
	public String post(@RequestBody String requestBody,
	                   @RequestParam("signature") String signature,
	                   @RequestParam("timestamp") String timestamp,
	                   @RequestParam("nonce") String nonce,
	                   @RequestParam("openid") String openId,
	                   @RequestParam(name = "encrypt_type", required = false) String encType,
	                   @RequestParam(name = "msg_signature", required = false) String msgSignature) {
		log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
						+ " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
				openId, signature, encType, msgSignature, timestamp, nonce, requestBody);
		if (!wxService.checkSignature(timestamp, nonce, signature)) {
			throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
		}
		String out = null;
		if (encType == null) {
			//明文模式
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}
			out = outMessage.toXml();
		} else if ("aes".equalsIgnoreCase(encType)) {
			//aes加密消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
					timestamp, nonce, msgSignature);
			log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}
			out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
		}
		log.debug("\n组装回复信息：{}", out);
		return out;
		
	}
	
	private WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
		try {
			return this.messageRouter.route(inMessage);
		} catch (Exception e) {
			log.error("路由消息时出现异常！", e);
		}
		
		return null;
	}
	
}
	
	

