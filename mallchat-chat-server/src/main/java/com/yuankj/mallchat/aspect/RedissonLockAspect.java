package com.yuankj.mallchat.aspect;

import cn.hutool.core.util.StrUtil;
import com.yuankj.mallchat.common.annocation.RedissonLock;
import com.yuankj.mallchat.common.service.LockService;
import com.yuankj.mallchat.common.utils.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Ykj
 * @date 2024-02-20/17:42
 * @apiNote
 */
@Slf4j
@Aspect
@Component
@Order(0)//确保比事务注解先执行，分布式锁在事务外
public class RedissonLockAspect {
	
	@Autowired
	private  LockService lockserivce;
	
	@Around("@annotation(com.yuankj.mallchat.common.annocation.RedissonLock)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Method method=((MethodSignature)point.getSignature()).getMethod();
		RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
		String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? SpElUtils.getMethodKey(method) : redissonLock.prefixKey();//默认方法限定名+注解排名（可能多个）
		String key = SpElUtils.parseSpEl(method, point.getArgs(), redissonLock.key());
		return lockserivce.executeWithLockThrows(prefix+":"+key,redissonLock.waitTime(),redissonLock.unit(),point::proceed);
	}
}
