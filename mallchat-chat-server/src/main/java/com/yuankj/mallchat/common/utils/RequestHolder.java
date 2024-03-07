package com.yuankj.mallchat.common.utils;


import com.yuankj.mallchat.common.domain.dto.RequestInfo;

/**
 * @author Ykj
 * @date 2023/10/8 0008/22:49
 * @apiNote
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
