package com.yuankj.mallchat.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ykj
 * @date 2023/10/9 0009/11:26
 * @apiNote
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_role")
public class UserRole implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * uid
     */
    @TableField("uid")
    private Long uid;
    
    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;
    
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
