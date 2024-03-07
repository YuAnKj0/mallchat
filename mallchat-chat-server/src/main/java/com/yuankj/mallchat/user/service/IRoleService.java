package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.user.domain.enums.RoleEnum;

public interface IRoleService {

    /**
     * 是否有某个权限，临时做法
     *
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);

}