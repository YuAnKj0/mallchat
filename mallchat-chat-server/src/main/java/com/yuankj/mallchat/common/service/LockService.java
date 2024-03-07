package com.yuankj.mallchat.common.service;

import com.yuankj.mallchat.common.exception.BusinessException;
import com.yuankj.mallchat.common.exception.CommonErrorEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Ykj
 * @date 2024-02-20/17:44
 * @apiNote
 */
@Service
@Slf4j
public class LockService {
	
	@Autowired
	private RedissonClient redissonClient;
	
	
	@SneakyThrows
	public  <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier){
         return executeWithLockThrows(key, waitTime, unit,supplier::get);
     }
	
	public <T> T executeWithLock(String key, Supplier<T> supplier) {
		return executeWithLock(key, -1, TimeUnit.MILLISECONDS, supplier);
	}
	
	public <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit,SupplierThrow<T> supplier)throws Throwable{
		RLock lock = redissonClient.getLock(key);
		boolean lockSuccess = lock.tryLock(waitTime, unit);
		if (!lockSuccess){
			throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
		}
				try {
            return supplier.get();//执行锁内的代码逻辑
        } finally {
					if (lock.isLocked()&&lock.isHeldByCurrentThread()){
						lock.unlock();
					}
        }
	}
	
	public interface SupplierThrow<T>{
		T get() throws Throwable;
	}
	
}
