package com.yuankj.mallchat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ykj
 * @date 2023/10/8 0008/22:17
 * @apiNote
 */

@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{
    
    SYSTEM_ERROR(-1,"系统出错了，请稍后再试"),
    PARAM_VALID(-2,"参数校验失败{0}"),
    FREQUENCY_LIMIT(-3,"请求太频繁了,请稍后再试哦~~"),
    LOCK_LIMIT(-4,"请求太频繁了,请稍后再试哦~~")
    ;
    
    private final Integer code;
    private final String msg;
    
    @Override
    public Integer getErrorCode() {
        return this.code;
    }
    
    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
