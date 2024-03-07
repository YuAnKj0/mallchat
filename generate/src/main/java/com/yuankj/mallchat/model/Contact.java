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
    * 会话列表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * uid
    */
    private Long uid;

    /**
    * 房间id
    */
    private Long roomId;

    /**
    * 阅读到的时间
    */
    private Date readTime;

    /**
    * 会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)
    */
    private Date activeTime;

    /**
    * 会话最新消息id
    */
    private Long lastMsgId;

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