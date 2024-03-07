package com.yuankj.mallchat.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024/02/26
 * @apiNote  角色枚举
 * 1. ADMIN 超级管理员
 * 2. CHAT_MANAGER 抹茶群聊管理
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
    ADMIN(1L, "超级管理员"),
    CHAT_MANAGER(2L, "抹茶群聊管理"),
    ;

    private final Long id;
    private final String desc;

    private static Map<Long, RoleEnum> cache;

    static {
        cache = Arrays.stream(RoleEnum.values()).collect(Collectors.toMap(RoleEnum::getId, Function.identity()));
    }

    public static RoleEnum of(Long type) {
        return cache.get(type);
    }
}