package com.yuankj.mallchat.websocket;

import cn.hutool.json.JSONUtil;
import com.yuankj.mallchat.user.domain.enums.WSBaseResp;
import com.yuankj.mallchat.user.domain.enums.WSReqTypeEnum;

import com.yuankj.mallchat.user.service.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ykj
 * @date 2024-03-25/11:46
 * @apiNote
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	private WebSocketService webSocketService;
	/**
	 * @param ctx 
	 * @param msg
	 * @throws Exception
	 * // 读取客户端发送的请求报文
	 * 
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		WSBaseResp wsBaseResp = JSONUtil.toBean(msg.text(), WSBaseResp.class);
		WSReqTypeEnum wsRespTypeEnum = WSReqTypeEnum.of(wsBaseResp.getType());
		switch (wsRespTypeEnum){
			case LOGIN:
				this.webSocketService.handleLoginReq(ctx.channel());
				log.info("请求二维码 = " + msg.text());
				break;
			case HEARTBEAT:
				break;
			default:
				log.info("未知类型");
		}
	}
}
