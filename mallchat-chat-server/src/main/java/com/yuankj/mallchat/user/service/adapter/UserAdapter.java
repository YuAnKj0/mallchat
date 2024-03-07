package com.yuankj.mallchat.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.yuankj.mallchat.common.domain.enums.YesOrNoEnum;
import com.yuankj.mallchat.user.domain.entity.ItemConfig;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.entity.UserBackpack;
import com.yuankj.mallchat.user.domain.vo.response.user.BadgeResp;
import com.yuankj.mallchat.user.domain.vo.response.user.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Ykj
 * @date 2023-10-20/12:16
 * @apiNote 用户适配器
 */
@Slf4j
public class UserAdapter {
    public static UserInfoResp buildUserInfoResp(User userInfo, Integer countByValidItemId) {
        UserInfoResp userInfoResp=new UserInfoResp();
        BeanUtil.copyProperties(userInfo,userInfoResp);
        userInfoResp.setModifyNameChance(countByValidItemId);
        return userInfoResp;
        
    }
    
    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        if (ObjectUtil.isNull(user) ) {
            return Collections.emptyList();
        }
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(a -> {
                    BadgeResp resp = new BadgeResp();
                    BeanUtil.copyProperties(a, resp);
                    resp.setObtain(obtainItemSet.contains(a.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    resp.setWearing(ObjectUtil.equal(a.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    return resp;
                }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
                        .thenComparing(BadgeResp::getObtain, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
	
	public static User buildAuthorizeUser(Long uid, WxOAuth2UserInfo userInfo) {
        User user = new User();
        user.setId(uid);
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setName(userInfo.getNickname());
        user.setSex(userInfo.getSex());
        if (userInfo.getNickname().length()>6) {
            user.setName("名字过长" + RandomUtil.randomInt(100000));
        }else {
            user.setName(userInfo.getNickname());
        }
        return user;
    }
}
