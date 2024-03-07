package com.yuankj.mallchat.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:14
 * @apiNote 
 */
 
  
/**
    * 黑名单
    */
@Data
@AllArgsConstructor
public class Black {
    /**
    * id
    */
    private Long id;

    /**
    * 拉黑目标类型 1.ip 2uid
    */
    private Integer type;

    /**
    * 拉黑目标
    */
    private String target;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;
}