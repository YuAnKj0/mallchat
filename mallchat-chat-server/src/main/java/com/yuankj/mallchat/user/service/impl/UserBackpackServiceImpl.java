package com.yuankj.mallchat.user.service.impl;

import com.yuankj.mallchat.common.annocation.RedissonLock;
import com.yuankj.mallchat.common.domain.enums.IdempotentEnum;
import com.yuankj.mallchat.common.domain.enums.YesOrNoEnum;
import com.yuankj.mallchat.common.event.ItemReceiveEvent;
import com.yuankj.mallchat.user.domain.dao.UserBackpackDao;
import com.yuankj.mallchat.user.domain.entity.ItemConfig;
import com.yuankj.mallchat.user.domain.entity.UserBackpack;
import com.yuankj.mallchat.user.domain.enums.ItemTypeEnum;
import com.yuankj.mallchat.user.service.IUserBackpackService;
import com.yuankj.mallchat.user.service.cache.ItemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Ykj
 * @date 2024-02-21/10:45
 * @apiNote
 */
@Service
public class UserBackpackServiceImpl implements IUserBackpackService {
	@Autowired
	@Lazy
	private UserBackpackServiceImpl userBackpackService;
	@Autowired
	private UserBackpackDao userBackpackDao;
	@Autowired
	private ItemCache itemCache;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	/**
	 * 获取背包中的物品
	 *
	 * @param uid            用户id
	 * @param itemId         物品id
	 * @param idempotentEnum 幂等类型
	 * @param businessId     上层业务发送的唯一标识
	 */
	@Override
	public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
		String idempotent = getIdempotent(itemId,idempotentEnum, businessId);
		userBackpackService.doAcquireItem(uid,itemId,idempotent);
	}
	
	
	@RedissonLock(key = "#idempotent",waitTime = 5000)
	public void doAcquireItem(Long uid, Long itemId, String idempotent) {
		UserBackpack userBackpack = userBackpackDao.getByIdp(idempotent);
		//幂等检查
		if (Objects.nonNull(userBackpack)){
			return;
		}
		//业务检查
		ItemConfig itemConfig = itemCache.getById(itemId);
		if (ItemTypeEnum.BADGE.getType().equals(itemConfig.getType())) {//徽章类型做唯一性检查
			Integer countByValidItemId = userBackpackDao.getCountByValidItemId(uid, itemId);
			if (countByValidItemId > 0) {//已经有徽章了不发
				return;
			}
		}
		//发物品
		UserBackpack insert = UserBackpack.builder()
				.uid(uid)
				.itemId(itemId)
				.status(YesOrNoEnum.NO.getStatus())
				.idempotent(idempotent)
				.build();
		userBackpackDao.save(insert);
		//用户收到物品的事件
		applicationEventPublisher.publishEvent(new ItemReceiveEvent(this,insert));
	}
	
	private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
		return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
		
	}
}
