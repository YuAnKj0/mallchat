package com.yuankj.mallchat.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.user.domain.entity.UserRole;
import com.yuankj.mallchat.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Ykj
 * @date 2023/10/9 0009/11:24
 * @apiNote
 */

@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {
    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, Objects.requireNonNull(uid))
                .list();
    }
    
}
