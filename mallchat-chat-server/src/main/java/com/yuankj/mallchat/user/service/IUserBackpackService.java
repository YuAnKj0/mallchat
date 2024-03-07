package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.common.domain.enums.IdempotentEnum;

/**
 * @author Ykj
 * @date 2024-02-21/10:46
 * @apiNote 用户背包表 服务类
 */

public interface IUserBackpackService {
	
	/**
	 *  获取背包中的物品
	 * @param uid 用户id
	 * @param itemId 物品id
	 * @param idempotentEnum 幂等类型
	 * @param businessId 上层业务发送的唯一标识
	 */
	void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId);
}
