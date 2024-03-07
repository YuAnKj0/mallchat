package com.yuankj.mallchat.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
/**
    * 本地消息表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecureInvokeRecord implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 请求快照参数json
    */
    private String secureInvokeJson;

    /**
    * 状态 1待执行 2已失败
    */
    private Byte status;

    /**
    * 下一次重试的时间
    */
    private Date nextRetryTime;

    /**
    * 已经重试的次数
    */
    private Integer retryTimes;

    /**
    * 最大重试次数
    */
    private Integer maxRetryTimes;

    /**
    * 执行失败的堆栈
    */
    private String failReason;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}