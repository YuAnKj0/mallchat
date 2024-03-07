package com.yuankj.mallchat.chat.service.strategy.msg;

import com.yuankj.mallchat.common.exception.CommonErrorEnum;
import com.yuankj.mallchat.common.utils.AssertUtil;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * @author Ykj
 * @date 2024-02-23/15:22
 * @apiNote
 */

public class MsgHandlerFactory {
	
	public static final Map<Integer,AbstractMsgHandler> STRATEGY_MAP=new HashedMap();
	
	public static void register(Integer code, AbstractMsgHandler strategy) {
		STRATEGY_MAP.put(code, strategy);
	}
	
	public static AbstractMsgHandler getStrategyNoNull(Integer code) {
		AbstractMsgHandler strategy = STRATEGY_MAP.get(code);
		AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
		return strategy;
	}
	
	
}
