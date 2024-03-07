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
    * 单聊房间表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFriend implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 房间id
    */
    private Long roomId;

    /**
    * uid1（更小的uid）
    */
    private Long uid1;

    /**
    * uid2（更大的uid）
    */
    private Long uid2;

    /**
    * 房间key由两个uid拼接，先做排序uid1_uid2
    */
    private String roomKey;

    /**
    * 房间状态 0正常 1禁用(删好友了禁用)
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