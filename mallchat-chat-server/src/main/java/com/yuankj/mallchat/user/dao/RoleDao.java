package com.yuankj.mallchat.user.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.user.domain.entity.Role;
import com.yuankj.mallchat.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-06-04
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}
