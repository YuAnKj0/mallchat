package com.yuankj.mallchat.user.service.cache;

import com.yuankj.mallchat.user.dao.ItemConfigDao;
import com.yuankj.mallchat.user.domain.entity.ItemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ykj
 * @date 2023-10-20/15:00
 * @apiNote
 */

@Component
@RequiredArgsConstructor
public class ItemCache {
	
	private final ItemConfigDao itemConfigDao;
	
	@Cacheable(cacheNames = "item", key = "'itemsByType:'+#type")
	public List<ItemConfig> getByType(Integer type) {
		return itemConfigDao.getByType(type);
	}
	
	@Cacheable(cacheNames = "item", key = "'item:'+#itemId")
	public ItemConfig getById(Long itemId) {
		return itemConfigDao.getById(itemId);
	}
}
