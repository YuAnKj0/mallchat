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
    * 用户表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
    * 用户id
    */
    private Long id;

    /**
    * 用户昵称
    */
    private String name;

    /**
    * 用户头像
    */
    private String avatar;

    /**
    * 性别 1为男性，2为女性
    */
    private Integer sex;

    /**
    * 微信openid用户标识
    */
    private String openId;

    /**
    * 在线状态 1在线 2离线
    */
    private Integer activeStatus;

    /**
    * 最后上下线时间
    */
    private Date lastOptTime;

    /**
    * ip信息
    */
    private String ipInfo;

    /**
    * 佩戴的徽章id
    */
    private Long itemId;

    /**
    * 使用状态 0.正常 1拉黑
    */
    private Integer status;

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