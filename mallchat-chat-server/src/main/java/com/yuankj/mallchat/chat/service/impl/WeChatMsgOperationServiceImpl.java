package com.yuankj.mallchat.chat.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import com.yuankj.mallchat.chat.service.WeChatMsgOperationService;
import com.yuankj.mallchat.common.domain.dto.FrequencyControlDTO;
import com.yuankj.mallchat.common.exception.FrequencyControlException;
import com.yuankj.mallchat.common.handler.GlobalUncaughtExceptionHandler;
import com.yuankj.mallchat.common.service.frequencycontrol.FrequencyControlUtil;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.service.cache.UserCache;
import com.yuankj.mallchat.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.yuankj.mallchat.common.service.frequencycontrol.FrequencyControlStrategyFactory.TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER;

/**
 * @author Ykj
 * @date 2024-03-25/9:18
 * @apiNote
 */
@Slf4j
@Component
public class WeChatMsgOperationServiceImpl implements WeChatMsgOperationService {
	private static final ExecutorService executor = new ThreadPoolExecutor(1, 10, 3000L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(20),
			new NamedThreadFactory("wechat-operation-thread", null, false,
					GlobalUncaughtExceptionHandler.getInstance()));
	// at消息的微信推送模板id
	private final String atMsgPublishTemplateId = "Xd7sWPZsuWa0UmpvLaZPvaJVjNj1KjEa0zLOm5_Z7IU";
	private final String WE_CHAT_MSG_COLOR = "#A349A4";
	
	@Autowired
	private UserCache userCache;
	@Autowired
	WxMpService wxMpService;
	/**
	 * 向被at的用户微信推送群聊消息
	 * @param senderUid
	 * @param receiverUidList
	 * @param msg
	 */
	@Override
	public void publishChatMsgToWeChatUser(long senderUid, List<Long> receiverUidList, String msg) {
		User sender = userCache.getUserInfo(senderUid);
		Set uidSet=new HashSet();
		uidSet.addAll(receiverUidList);
		Map<Long, User> userMap=userCache.getUserInfoBatch(uidSet);
		userMap.values().forEach(user->{
			if (Objects.nonNull(user.getOpenId())) {
				executor.execute(()->{
					WxMpTemplateMessage msgTemplate = getAtMsgTemplate(sender, user.getOpenId(), msg);
					publishTemplateMsgCheckLimit(msgTemplate);
				});
			}
		});
	}
	
	private void publishTemplateMsgCheckLimit(WxMpTemplateMessage msgTemplate) {
		try {
			FrequencyControlDTO frequencyControlDTO = new FrequencyControlDTO();
			frequencyControlDTO.setKey("TemplateMsg:" + msgTemplate.getToUser());
			frequencyControlDTO.setUnit(TimeUnit.HOURS);
			frequencyControlDTO.setCount(1);
			frequencyControlDTO.setTime(1);
			FrequencyControlUtil.executeWithFrequencyControl(TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER, frequencyControlDTO,
					() -> publishTemplateMsg(msgTemplate));
		} catch (FrequencyControlException e) {
			log.info("wx push limit openid:{}", msgTemplate.getToUser());
		} catch (Throwable e) {
			log.error("wx push error openid:{}", msgTemplate.getToUser());
		}
	}
	
	private void publishTemplateMsg(WxMpTemplateMessage templateMsg) {
		WxMpTemplateMsgService wxMpTemplateMsgService = wxMpService.getTemplateMsgService();//todo 等审核通过
        try {
            wxMpTemplateMsgService.sendTemplateMsg(templateMsg);
        } catch (WxErrorException e) {
            log.error("publish we chat msg failed! open id is {}, msg is {}.",
                    templateMsg.getToUser(), JsonUtils.toStr(templateMsg.getData()));
        }
	}
	
	private WxMpTemplateMessage getAtMsgTemplate(User sender, String openId, String msg) {
		return WxMpTemplateMessage.builder()
				.toUser(openId)
				.templateId(atMsgPublishTemplateId)
				.data(generateAtMsgData(sender,msg)).build();
	}
	
	private List<WxMpTemplateData> generateAtMsgData(User sender, String msg) {
		List dataList=new ArrayList<WxMpTemplateData>();
		dataList.add(new WxMpTemplateData("name", sender.getName(), WE_CHAT_MSG_COLOR));
		dataList.add(new WxMpTemplateData("content", msg, WE_CHAT_MSG_COLOR));
		return dataList;
	}
}
