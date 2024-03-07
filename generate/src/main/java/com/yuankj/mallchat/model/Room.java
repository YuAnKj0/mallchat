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
    * 房间表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 房间类型 1群聊 2单聊
    */
    private Integer type;

    /**
    * 是否全员展示 0否 1是
    */
    private Integer hotFlag;

    /**
    * 群最后消息的更新时间（热点群不需要写扩散，只更新这里）
    */
    private Date activeTime;

    /**
    * 会话中的最后一条消息id
    */
    private Long lastMsgId;

    /**
    * 额外信息（根据不同类型房间有不同存储的东西）
    */
    private String extJson;

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