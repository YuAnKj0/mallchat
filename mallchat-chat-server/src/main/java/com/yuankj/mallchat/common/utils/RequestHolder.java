package com.yuankj.mallchat.common.utils;


import com.yuankj.mallchat.common.domain.dto.RequestInfo;

/**
 * @author Ykj
 * @date 2023/10/8 0008/22:49
 * @apiNote 请求上下文
 * description 用于存储和传递请求相关的信息，例如请求的参数、请求头、请求体等。
 */

public class RequestHolder {
    
    private static final ThreadLocal<RequestInfo> THREAD_LOCAL=new ThreadLocal<>();
    
    public static void set(RequestInfo requestInfo){
        THREAD_LOCAL.set(requestInfo);
    }
    public static RequestInfo get(){
        return THREAD_LOCAL.get();
    }
    
    public static void remove(){
        THREAD_LOCAL.remove();
    }
    
}
