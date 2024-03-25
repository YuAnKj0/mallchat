package com.yuankj.mallchat.websocket;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author Ykj
 * @date 2024-03-25/11:44
 * @apiNote
 */

public class NettyUtil {
	
	public static AttributeKey<String> TOKEN =AttributeKey.valueOf("userId");
	public static AttributeKey<String> IP = AttributeKey.valueOf("ip");
	public static AttributeKey<Long> UID = AttributeKey.valueOf("uid");
	public static AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
	
	public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
		Attribute<T> attr = channel.attr(attributeKey);
		attr.set(data);
	}
	
	public static <T> T getAttr(Channel channel, AttributeKey<T> ip) {
		return channel.attr(ip).get();
	}
	
	
}
