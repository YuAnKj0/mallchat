package com.yuankj.mallchat.chat.service.strategy.mark;

import com.yuankj.mallchat.common.exception.CommonErrorEnum;
import com.yuankj.mallchat.common.utils.AssertUtil;

import java.util.HashMap;
import java.util.Map;

public class MsgMarkFactory {
    private static final Map<Integer, AbstractMsgMarkStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer markType, AbstractMsgMarkStrategy strategy) {
        STRATEGY_MAP.put(markType, strategy);
    }

    public static AbstractMsgMarkStrategy getStrategyNoNull(Integer markType) {
        AbstractMsgMarkStrategy strategy = STRATEGY_MAP.get(markType);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
        return strategy;
    }
}