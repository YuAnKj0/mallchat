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
    * 用户联系人表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFriend implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * uid
    */
    private Long uid;

    /**
    * 好友uid
    */
    private Long friendUid;

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