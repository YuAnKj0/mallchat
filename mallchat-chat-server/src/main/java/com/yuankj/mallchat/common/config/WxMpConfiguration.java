package com.yuankj.mallchat.common.config;

import com.yuankj.mallchat.user.service.handler.LogHandler;
import com.yuankj.mallchat.user.service.handler.MsgHandler;
import com.yuankj.mallchat.user.service.handler.SubscribeHandler;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * @author Ykj
 * @date 2024-03-25/14:40
 * @apiNote
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {
	
	private final LogHandler logHandler;
	private final MsgHandler msgHandler;
	private final SubscribeHandler subscribeHandler;
	private final ScanHandler scanHandler;
	private final WxMpProperties properties;
	
	@Bean
	public WxMpService wxMpService() {
		final List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
		if (configs==null) {
			throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
		}
		WxMpService service = new WxMpServiceImpl();
		service.setMultiConfigStorages(configs.stream().map(a->{
			WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
			configStorage.setAppId(a.getAppId());
			configStorage.setSecret(a.getSecret());
			configStorage.setToken(a.getToken());
			configStorage.setAesKey(a.getAesKey());
			return configStorage;
				}).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a->a, (k1, k2)->k1)));
		return service;		
	}
	
	@Bean
	public WxMpMessageRouter messageRouter(WxMpService wxMpService){
		final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
		// 记录所有事件的日志 （异步执行）
		newRouter.rule().handler(this.logHandler).next();
		// 关注事件
		newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();
		
		// 扫码事件
		newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();
		
		// 默认
		newRouter.rule().async(false).handler(this.msgHandler).end();
		
		return newRouter;
	}
}
