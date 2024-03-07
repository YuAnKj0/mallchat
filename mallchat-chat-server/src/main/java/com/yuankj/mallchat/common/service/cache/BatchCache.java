package com.yuankj.mallchat.common.service.cache;

import java.util.List;
import java.util.Map;

/**
 * @author Ykj
 * @date 2023-10-20/13:05
 * @apiNote
 */

public interface BatchCache<IN,OUT> {
    
    /**
     * 获取单个
     */
    OUT get(IN req);
    
    /**
     * 获取批量
     */
    Map<IN, OUT> getBatch(List<IN> req);
    
    /**
     * 修改删除单个
     */
    void delete(IN req);
    
    /**
     * 修改删除多个
     */
    void deleteBatch(List<IN> req);
}
