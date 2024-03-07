package com.yuankj.mallchat.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ykj
 * @date 2023/10/9 0009/18:15
 * @apiNote
 */
@AllArgsConstructor
@Getter
public enum BlackTypeEnum {
    IP(1),
    UID(2),
    ;
    
    private final Integer type;
}
