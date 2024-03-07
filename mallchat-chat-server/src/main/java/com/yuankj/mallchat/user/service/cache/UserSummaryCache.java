package com.yuankj.mallchat.user.service.cache;

import com.yuankj.mallchat.common.constant.RedisKey;
import com.yuankj.mallchat.common.service.cache.AbstractRedisStringCache;
import com.yuankj.mallchat.user.domain.dao.SummeryInfoDTO;
import com.yuankj.mallchat.user.domain.dao.UserBackpackDao;
import com.yuankj.mallchat.user.domain.entity.*;
import com.yuankj.mallchat.user.domain.enums.ItemTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2023-10-20/13:03
 * @apiNote
 */

@Component
@RequiredArgsConstructor
public class UserSummaryCache extends AbstractRedisStringCache<Long,SummeryInfoDTO>{
    
    private final UserInfoCache userInfoCache;
    private final ItemCache itemCache;
    private final UserBackpackDao userBackpackDao;
    
    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING,uid);
    }
    
    @Override
    protected Long getExpireSeconds() {
        return 10 * 60L;
    }
    
    @Override
    protected Map<Long, SummeryInfoDTO> load(List<Long> uidList) {
        //用户基本信息
        Map<Long, User> userMap= userInfoCache.getBatch(uidList);
        //用户徽章信息
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        List<Long> itemIds = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uidList,itemIds);
        Map<Long, List<UserBackpack>> userBadgeMap = backpacks.stream().collect(Collectors.groupingBy(UserBackpack::getUid));
        //用户最后一次更新时间
        return uidList.stream().map(uid ->{
            SummeryInfoDTO summeryInfoDTO = new SummeryInfoDTO();
            User user = userMap.get(uid);
            if (Objects.isNull(user)) {
                return null;
            }
            List<UserBackpack> userBackpacks = userBadgeMap.getOrDefault(user.getId(), new ArrayList<>());
            summeryInfoDTO.setAvatar(user.getAvatar());
            summeryInfoDTO.setUid(user.getId());
            summeryInfoDTO.setName(user.getName());
            summeryInfoDTO.setLocPlace(Optional.ofNullable(user.getIpInfo()).map(IpInfo::getUpdateIpDetail).map(IpDetail::getCity).orElse(null));
            summeryInfoDTO.setWearingItemId(user.getItemId());
            summeryInfoDTO.setItemIds(userBackpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toList()));
            return summeryInfoDTO;
        }).filter(Objects::nonNull).collect(Collectors.toMap(SummeryInfoDTO::getUid, Function.identity()));
    }
    
    /**
     * 获取单个
     *
     * @param req
     */
    @Override
    public SummeryInfoDTO get(Long req) {
        return null;
    }
    
    /**
     * 获取批量
     *
     * @param req
     */
    @Override
    public Map<Long, SummeryInfoDTO> getBatch(List<Long> req) {
        return null;
    }
    
    /**
     * 修改删除单个
     *
     * @param req
     */
    @Override
    public void delete(Long req) {
        
    }
    
    /**
     * 修改删除多个
     *
     * @param req
     */
    @Override
    public void deleteBatch(List<Long> req) {
        
    }
}
