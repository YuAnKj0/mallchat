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
    * 用户申请表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApply implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 申请人uid
    */
    private Long uid;

    /**
    * 申请类型 1加好友
    */
    private Integer type;

    /**
    * 接收人uid
    */
    private Long targetId;

    /**
    * 申请信息
    */
    private String msg;

    /**
    * 申请状态 1待审批 2同意
    */
    private Integer status;

    /**
    * 阅读状态 1未读 2已读
    */
    private Integer readStatus;

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