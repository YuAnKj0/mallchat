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
    * 微信消息表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMsg implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 微信openid用户标识
    */
    private String openId;

    /**
    * 用户消息
    */
    private String msg;

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