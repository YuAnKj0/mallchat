package com.yuankj.mallchat.user.service.handler;

import cn.hutool.json.JSONUtil;
import com.yuankj.mallchat.chat.dao.WxMsgDao;
import com.yuankj.mallchat.chat.domain.entity.WxMsg;
import com.yuankj.mallchat.user.service.adapter.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ykj
 * @date 2024-03-25/14:53
 * @apiNote
 */
@Component
public class MsgHandler extends AbstractHandler{
	@Autowired
	private WxMsgDao wxMsgDao;
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
		if (true){
			WxMsg wxMsg = new WxMsg();
			wxMsg.setMsg(wxMpXmlMessage.getContent());
			wxMsg.setOpenId(wxMpXmlMessage.getFromUser());
			wxMsgDao.save(wxMsg);
			return null;
		}
		if (!wxMpXmlMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)){
			//可以选择将消息保存到本地
		}
		//当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		try {
			if (StringUtils.startsWithAny(wxMpXmlMessage.getContent(), "你好", "客服")
					&& wxMpService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
				return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
						.fromUser(wxMpXmlMessage.getToUser())
						.toUser(wxMpXmlMessage.getFromUser()).build();
			}
			
		} catch (WxErrorException e) {
			//错误处理
			e.printStackTrace();
		}
		//组装回复消息
		String content = "收到信息内容：" + JSONUtil.toJsonStr(wxMpXmlMessage);
		return new TextBuilder().build(content, wxMpXmlMessage, wxMpService);
	}
}
