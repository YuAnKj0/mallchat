package com.yuankj.mallchat.user.dao;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.common.domain.enums.NormalOrNoEnum;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.CursorUtils;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.enums.ChatActiveStatusEnum;
import com.yuankj.mallchat.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ykj
 * @date 2023/10/8 0008/23:23
 * @apiNote
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {
	
	public User getByName(String name) {
		return lambdaQuery().eq(User::getName,name).one();
	}
	
	public void modifyName(Long uid, String name) {
		User update = new User();
		update.setId(uid);
		update.setName(name);
		updateById(update);
		
	}
	
	public User getByOpenId(String openId) {
		LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getOpenId, openId);
		return getOne(eq);
	}
	
	public void wearingBadge(Long uid, Long badgeId) {
		User update = new User();
		update.setId(uid);
		update.setItemId(badgeId);
		updateById(update);
	}
	
	public List<User> getFriendList(List<Long> friendUids) {
		return lambdaQuery().in(User::getId, friendUids)
				.select(User::getId,User::getActiveStatus,User::getName,User::getAvatar).list();
	}
	
	public Number getOnlineCount(List<Long> memberUidList) {
		return lambdaQuery().eq(User::getActiveStatus, ChatActiveStatusEnum.ONLINE.getStatus())
				.in(CollectionUtil.isNotEmpty(memberUidList),User::getId,memberUidList).count();
	}
	
	public CursorPageBaseResp<User> getCursorPage(List<Long> memberUidList, CursorPageBaseReq request, ChatActiveStatusEnum online) {
		return CursorUtils.getCursorPageByMysql(this,request,wrapper ->{
			wrapper.eq(User::getActiveStatus,online.getStatus());
			wrapper.in(CollectionUtils.isNotEmpty(memberUidList),User::getId,memberUidList);//普通群对uid列表做限制
		},User::getLastOptTime);
	}
	
	public List<User> getMemberList() {
		return lambdaQuery().eq(User::getStatus, NormalOrNoEnum.NORMAL.getStatus())
				////最近活跃的1000个人，可以用lastOptTime字段，但是该字段没索引，updateTime可平替
				.orderByDesc(User::getLastOptTime)
				.last("limit 1000")
				.select(User::getId,User::getName,User::getAvatar).list();
	}
	
}
