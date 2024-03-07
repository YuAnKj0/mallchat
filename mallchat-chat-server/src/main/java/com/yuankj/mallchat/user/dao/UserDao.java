package com.yuankj.mallchat.user.dao;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.user.domain.entity.User;
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
}
