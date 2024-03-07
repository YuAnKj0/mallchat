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
    * 群成员表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 群组id
    */
    private Long groupId;

    /**
    * 成员uid
    */
    private Long uid;

    /**
    * 成员角色 1群主 2管理员 3普通成员
    */
    private Integer role;

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