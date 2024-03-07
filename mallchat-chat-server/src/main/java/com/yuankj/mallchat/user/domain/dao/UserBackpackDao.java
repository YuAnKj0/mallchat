package com.yuankj.mallchat.user.domain.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.common.domain.enums.YesOrNoEnum;
import com.yuankj.mallchat.user.domain.entity.UserBackpack;
import com.yuankj.mallchat.user.mapper.UserBackpackMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> {
    
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        
        return lambdaQuery().eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
    }
	
	public List<UserBackpack> getByItemIds(List<Long> uids, List<Long> itemIds) {
		return lambdaQuery().in(UserBackpack::getUid, uids)
				.in(UserBackpack::getItemId, itemIds)
				.eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
				.list();
	}
	public List<UserBackpack> getByItemIds(Long uid, List<Long> itemIds) {
		return lambdaQuery().in(UserBackpack::getUid, uid)
				.in(UserBackpack::getItemId, itemIds)
				.eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
				.list();
	}
	
	public UserBackpack getFirstValidItem(Long uid, Long itemId) {
		LambdaQueryWrapper<UserBackpack> queryWrapper = new QueryWrapper<UserBackpack>().lambda()
				.eq(UserBackpack::getId,uid)
				.eq(UserBackpack::getItemId,itemId).eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
				.last("limit 1");
		return getOne(queryWrapper);
	}
	
	public boolean invalidItem(Long id) {
		UserBackpack userBackpack = new UserBackpack();
		userBackpack.setId(id);
		userBackpack.setStatus(YesOrNoEnum.YES.getStatus());
		return updateById(userBackpack);
	}
	
	public UserBackpack getByIdp(String idempotent) {
		return lambdaQuery().eq(UserBackpack::getIdempotent, idempotent).one();
	}
}