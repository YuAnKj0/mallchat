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
    * 群聊房间表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomGroup implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 房间id
    */
    private Long roomId;

    /**
    * 群名称
    */
    private String name;

    /**
    * 群头像
    */
    private String avatar;

    /**
    * 额外信息（根据不同类型房间有不同存储的东西）
    */
    private String extJson;

    /**
    * 逻辑删除(0-正常,1-删除)
    */
    private Integer deleteStatus;

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