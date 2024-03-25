package com.yuankj.mallchat.user.service;

import io.netty.channel.Channel;

/**
 * @author Ykj
 * @date 2024-03-25/11:22
 * @apiNote
 */

public interface WebSocketService {
	void handleLoginReq(Channel channel);
}
