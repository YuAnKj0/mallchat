package com.yuankj.mallchat.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2023/10/9 0009/18:35
 * @apiNote
 */
@AllArgsConstructor
@Getter
public enum MessageStatusEnum {
    NORMAL(0, "正常"),
    DELETE(1, "删除"),
    ;
    
    private final Integer status;
    private final String desc;
    
    private static Map<Integer, MessageStatusEnum> cache;
    
    static {
        cache = Arrays.stream(MessageStatusEnum.values()).collect(Collectors.toMap(MessageStatusEnum::getStatus, Function.identity()));
    }
    
    public static MessageStatusEnum of(Integer type) {
        return cache.get(type);
    }

}
