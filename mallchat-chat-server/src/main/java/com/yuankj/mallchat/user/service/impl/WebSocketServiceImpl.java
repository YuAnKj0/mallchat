package com.yuankj.mallchat.user.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yuankj.mallchat.common.constant.RedisKey;
import com.yuankj.mallchat.common.utils.RedisUtils;
import com.yuankj.mallchat.user.domain.dto.WSChannelExtraDTO;
import com.yuankj.mallchat.user.domain.enums.WSBaseResp;
import com.yuankj.mallchat.user.service.WebSocketService;
import com.yuankj.mallchat.user.service.adapter.WSAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Ykj
 * @date 2024-03-25/11:22
 * @apiNote
 */
@Component
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
	@Autowired
	private WxMpService wxMpService;
	private static final Duration EXPIRE_TIME = Duration.ofHours(1);
	private static final Long MAX_MUM_SIZE = 10000L;
	/**
	 * 所有请求登录的code与channel关系
	 */
	public static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
			.expireAfterWrite(EXPIRE_TIME)
			.maximumSize(MAX_MUM_SIZE)
			.build();
	/**
	 * 所有已连接的websocket连接列表和一些额外参数
	 */
	private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
	/**
	 * 所有在线的用户和对应的socket
	 */
	private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Channel, WSChannelExtraDTO> getOnlineMap() {
		return ONLINE_WS_MAP;
	}
	
	/**
	 * redis保存loginCode的key
	 */
	private static final String LOGIN_CODE = "loginCode";
	/**
	 * 处理用户登录请求，需要返回一张带code的二维码
	 * @param channel 
	 */
	@SneakyThrows
	@Override
	public void handleLoginReq(Channel channel) {
		//生成随机不重复的登录码,并将channel存在本地cache中
		Integer code=generateLoginCode(channel);
		//请求微信接口，获取登录码地址
		WxMpQrCodeTicket wxMpQrCodeTicket= wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code,  (int) EXPIRE_TIME.getSeconds());
		//返回给前端（channel必在本地）
		sendMsg(channel, WSAdapter.buildLoginResp(wxMpQrCodeTicket));
	}
	
	/**
	 * 获取不重复的登录的code，微信要求最大不超过int的存储极限 防止并发，可以给方法加上synchronize，也可以使用cas乐观锁
	 * @param channel
	 * @return
	 */
	private Integer generateLoginCode(Channel channel) {
		int inc;
		do {
			//本地cache时间必须比redis key过期时间短，否则会出现并发问题
			inc=RedisUtils.integerInc(RedisKey.getKey(LOGIN_CODE),(int) EXPIRE_TIME.toMinutes(), TimeUnit.MINUTES);
		}while (WAIT_LOGIN_MAP.asMap().containsKey(inc));
		//储存一份在本地
		WAIT_LOGIN_MAP.put(inc, channel);
		return inc;
	}
	private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp) {
		channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
	}
}
