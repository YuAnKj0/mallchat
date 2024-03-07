package com.yuankj.mallchat.user.service.cache;

import com.yuankj.mallchat.common.constant.RedisKey;
import com.yuankj.mallchat.common.service.cache.AbstractRedisStringCache;
import com.yuankj.mallchat.user.dao.UserDao;
import com.yuankj.mallchat.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2023-10-20/14:55
 * @apiNote
 */
@Component
@RequiredArgsConstructor
public class UserInfoCache  extends AbstractRedisStringCache<Long, User> {
	
	private final UserDao userDao;
	
	@Override
	protected String getKey(Long uid) {
		return RedisKey.getKey(RedisKey.USER_INFO_STRING,uid);
	}
	
	@Override
	protected Long getExpireSeconds() {
		return 5 * 60L;
	}
	
	@Override
	protected Map<Long, User> load(List<Long> uidList) {
		List<User> needLoadUserList=userDao.listByIds(uidList);
		return needLoadUserList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
	}
}
