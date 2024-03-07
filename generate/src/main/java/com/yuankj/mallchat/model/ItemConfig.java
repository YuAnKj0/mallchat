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
    * 功能物品配置表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemConfig implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
    * 物品类型 1改名卡 2徽章
    */
    private Integer type;

    /**
    * 物品图片
    */
    private String img;

    /**
    * 物品功能描述
    */
    private String describe;

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