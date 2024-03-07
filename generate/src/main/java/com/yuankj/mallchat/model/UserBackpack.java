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
    * 用户背包表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBackpack implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * uid
    */
    private Long uid;

    /**
    * 物品id
    */
    private Integer itemId;

    /**
    * 使用状态 0.待使用 1已使用
    */
    private Integer status;

    /**
    * 幂等号
    */
    private String idempotent;

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