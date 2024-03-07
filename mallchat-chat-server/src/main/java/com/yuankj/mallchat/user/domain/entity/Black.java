package com.yuankj.mallchat.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ykj
 * @date 2023/10/8 0008/23:26
 * @apiNote
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("black")
public class Black implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 拉黑目标类型 1.ip 2uid
     *
     * @see com.yuankj.mallchat.user.domain.enums.BlackTypeEnum
     */
    @TableField("type")
    private Integer type;
    
    /**
     * 拉黑目标
     */
    @TableField("target")
    private String target;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
}
