package com.yuankj.mallchat.config;

import com.yuankj.mallchat.annotation.SecureInvokeConfigurer;
import com.yuankj.mallchat.aspect.SecureInvokeAspect;
import com.yuankj.mallchat.dao.SecureInvokeRecordDao;
import com.yuankj.mallchat.mapper.SecureInvokeRecordMapper;
import com.yuankj.mallchat.service.MQProducer;
import com.yuankj.mallchat.service.SecureInvokeService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;
import org.springframework.util.function.SingletonSupplier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-03-18/14:45
 * @apiNote
 */
@Configuration
@EnableScheduling
@MapperScan(basePackageClasses = SecureInvokeRecordMapper.class)
@Import({SecureInvokeAspect.class, SecureInvokeRecordDao.class})
public class TransactionAutoConfiguration {
	
	@Nullable
	protected Executor executor;
	@Autowired
	void setConfigurers(ObjectProvider<SecureInvokeConfigurer> configurers){
		Supplier<SecureInvokeConfigurer> configurer = SingletonSupplier.of(() -> {
			List<SecureInvokeConfigurer> candidates = configurers.stream().collect(Collectors.toList());
			if (CollectionUtils.isEmpty(candidates)) {
				return null;
			}
			if (candidates.size() > 1) {
				throw new IllegalStateException("Only one SecureInvokeConfigurer may exist");
			}
			return candidates.get(0);
		});
		executor = Optional.ofNullable(configurer.get()).map(SecureInvokeConfigurer::getSecureInvokeExecutor).orElse(ForkJoinPool.commonPool());
	}
	
	@Bean
	public SecureInvokeService getSecureInvokeService(SecureInvokeRecordDao dao) {
		return new SecureInvokeService(dao, executor);
	}
	
	
	@Bean
	public MQProducer getMQProducer() {
		return new MQProducer();
	}
}
