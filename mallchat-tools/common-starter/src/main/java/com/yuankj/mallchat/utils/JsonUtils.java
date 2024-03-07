package com.yuankj.mallchat.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Ykj
 * @date 2023-10-18/14:56
 * @apiNote
 */

public class JsonUtils {
    private static final ObjectMapper jsonMapper=new ObjectMapper();
    
    
    public static <T> T toObj(String str, Class<T> clz) {
        
        try {
            return jsonMapper.readValue(str,clz);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    
    public static String toStr(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
