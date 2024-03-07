package com.yuankj.mallchat.common.event;

import com.yuankj.mallchat.user.domain.entity.UserBackpack;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Ykj
 * @date 2024-02-21/11:08
 * @apiNote
 */
@Getter
public class ItemReceiveEvent extends ApplicationEvent {
	private UserBackpack userBackpack;
	
		public ItemReceiveEvent(Object source, UserBackpack userBackpack) {
        super(source);
		this.userBackpack = userBackpack;
    }
}

