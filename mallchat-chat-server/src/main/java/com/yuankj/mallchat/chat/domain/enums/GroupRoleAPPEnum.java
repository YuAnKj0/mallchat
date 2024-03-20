package com.yuankj.mallchat.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 群成员角色枚举
 */
@AllArgsConstructor
@Getter
public enum GroupRoleAPPEnum {
	LEADER(1, "群主"),
	MANAGER(2, "管理"),
	MEMBER(3, "普通成员"),
	REMOVE(4, "被移除的成员"),
	;
	
	private static final Map<Integer, GroupRoleAPPEnum> cache;

	static {
		cache = Arrays.stream(GroupRoleAPPEnum.values()).collect(Collectors.toMap(GroupRoleAPPEnum::getType, Function.identity()));
	}
	
	private final Integer type;
	private final String desc;
	
	public static GroupRoleAPPEnum of(Integer type) {
		return cache.get(type);
	}
}