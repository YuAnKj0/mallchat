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
    * 用户角色关系表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * uid
    */
    private Long uid;

    /**
    * 角色id
    */
    private Long roleId;

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