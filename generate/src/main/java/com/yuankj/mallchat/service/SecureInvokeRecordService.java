package com.yuankj.mallchat.service;

import com.yuankj.mallchat.model.SecureInvokeRecord;
    /**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
public interface SecureInvokeRecordService{


    int deleteByPrimaryKey(Long id);

    int insert(SecureInvokeRecord record);

    int insertSelective(SecureInvokeRecord record);

    SecureInvokeRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SecureInvokeRecord record);

    int updateByPrimaryKey(SecureInvokeRecord record);

}
