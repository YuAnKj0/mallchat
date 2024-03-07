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
    * 消息表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 会话表id
    */
    private Long roomId;

    /**
    * 消息发送者uid
    */
    private Long fromUid;

    /**
    * 消息内容
    */
    private String content;

    /**
    * 回复的消息内容
    */
    private Long replyMsgId;

    /**
    * 消息状态 0正常 1删除
    */
    private Integer status;

    /**
    * 与回复的消息间隔多少条
    */
    private Integer gapCount;

    /**
    * 消息类型 1正常文本 2.撤回消息
    */
    private Integer type;

    /**
    * 扩展信息
    */
    private String extra;

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