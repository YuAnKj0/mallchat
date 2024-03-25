package com.yuankj.mallchat.websocket;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ykj
 * @date 2024-03-25/11:45
 * @apiNote
 */
@Slf4j
@Configuration
public class NettyWebSocketServer {
	public static final int WEB_SOCKET_PORT = 8090;
	public static final NettyWebSocketServerHandler NETTY_WEB_SOCKET_SERVER_HANDLER = new NettyWebSocketServerHandler();
	// 创建线程池执行器
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());
}
