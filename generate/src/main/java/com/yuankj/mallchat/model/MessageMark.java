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
    * 消息标记表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageMark implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 消息表id
    */
    private Long msgId;

    /**
    * 标记人uid
    */
    private Long uid;

    /**
    * 标记类型 1点赞 2举报
    */
    private Integer type;

    /**
    * 消息状态 0正常 1取消
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