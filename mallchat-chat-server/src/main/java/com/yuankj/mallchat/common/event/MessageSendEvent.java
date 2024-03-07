package com.yuankj.mallchat.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Ykj
 * @date 2024-02-26/9:15
 * @apiNote
 */

@Getter
public class MessageSendEvent extends ApplicationEvent {
	private Long msgId;
	
	public MessageSendEvent(Object source, Long msgId) {    
		super(source);
		this.msgId = msgId;
	}
	
}
