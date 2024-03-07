package com.yuankj.mallchat.user.service.impl;


import cn.hutool.core.util.StrUtil;
import com.yuankj.mallchat.common.constant.RedisKey;
import com.yuankj.mallchat.common.utils.JwtUtils;
import com.yuankj.mallchat.common.utils.RedisUtils;
import com.yuankj.mallchat.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author Ykj
 * @date 2024-02-21/11:22
 * @apiNote
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	
	//token过期时间
	private static final Integer TOKEN_EXPIRE_DAYS = 5;
	//token续期时间
	private static final Integer TOKEN_RENEWAL_DAYS = 2;
	@Autowired
	private JwtUtils jwtUtils;
	
	
	/**
	 * 校验token是不是有效
	 *
	 * @param token
	 * @return
	 */
	@Override
	public boolean verify(String token) {
		Long uid = jwtUtils.getUidOrNull(token);
		if (Objects.nonNull(uid)) {
			return false;
		}
		String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
		String str = RedisUtils.getStr(key);
		return Objects.equals(str, token);
	}
	
	/**
	 * 刷新token有效期
	 *
	 * @param token
	 */
	@Override
	public void renewalTokenIfNecessary(String token) {
		Long uid = jwtUtils.getUidOrNull(token);
		if (Objects.nonNull(uid)) {
			return;
		}
		String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
		Long expireDays = RedisUtils.getExpire(key, TimeUnit.DAYS);
		if (expireDays==-2) { //不存在的key
			return;
		}
		if (expireDays<TOKEN_RENEWAL_DAYS) {//小于一天的token帮忙续期
			RedisUtils.expire(key, TOKEN_RENEWAL_DAYS, TimeUnit.DAYS);
		}
		
	}
	
	/**
	 * 登录成功，获取token
	 *
	 * @param uid
	 * @return 返回token
	 */
	@Override
	public String login(Long uid) {
		String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
		String token = RedisUtils.getStr(key);
		if (StrUtil.isNotBlank(token)) {
			return token;
		}
		//获取用户token
		token = jwtUtils.createToken(uid);
		RedisUtils.set(key, token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
		return token;
	}
	
	/**
	 * 如果token有效，返回uid
	 *
	 * @param token
	 * @return
	 */
	@Override
	public Long getValidUid(String token) {
		boolean verify = verify(token);
		
		return verify?jwtUtils.getUidOrNull(token):null;
	}
	public static void main(String[] args) {
		System.out.println();
	}
}
