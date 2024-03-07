package com.yuankj.mallchat.user.service.impl;

import com.yuankj.mallchat.common.event.UserBlackEvent;
import com.yuankj.mallchat.common.event.UserRegisterEvent;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.common.utils.sensitiveWord.SensitiveWordBs;
import com.yuankj.mallchat.user.dao.BlackDao;
import com.yuankj.mallchat.user.dao.ItemConfigDao;
import com.yuankj.mallchat.user.dao.UserDao;
import com.yuankj.mallchat.user.domain.dao.SummeryInfoDTO;
import com.yuankj.mallchat.user.domain.dao.UserBackpackDao;
import com.yuankj.mallchat.user.domain.dto.ItemInfoDTO;
import com.yuankj.mallchat.user.domain.entity.Black;
import com.yuankj.mallchat.user.domain.entity.ItemConfig;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.entity.UserBackpack;
import com.yuankj.mallchat.user.domain.enums.BlackTypeEnum;
import com.yuankj.mallchat.user.domain.enums.ItemEnum;
import com.yuankj.mallchat.user.domain.enums.ItemTypeEnum;
import com.yuankj.mallchat.user.domain.vo.request.user.*;
import com.yuankj.mallchat.user.domain.vo.response.user.BadgeResp;
import com.yuankj.mallchat.user.domain.vo.response.user.UserInfoResp;
import com.yuankj.mallchat.user.service.UserService;
import com.yuankj.mallchat.user.service.adapter.UserAdapter;
import com.yuankj.mallchat.user.service.cache.ItemCache;
import com.yuankj.mallchat.user.service.cache.UserCache;
import com.yuankj.mallchat.user.service.cache.UserSummaryCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2023-10-18/14:26
 * @apiNote
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private final UserCache userCache;
	@Autowired
	private final UserBackpackDao userBackpackDao;
	@Autowired
	private final UserSummaryCache userSummaryCache;
	@Autowired
	private final ItemCache itemCache;
	@Autowired
	private final SensitiveWordBs sensitiveWordBs;
	@Autowired
	private final UserDao userDao;
	@Autowired
	private final ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private ItemConfigDao itemConfigDao;
    @Autowired
    private BlackDao blackDao;
	
	
	@Override
	public UserInfoResp getUserInfo(Long uid) {
		User userInfo = userCache.getUserInfo(uid);
		Integer countByValidItemId = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
		return UserAdapter.buildUserInfoResp(userInfo, countByValidItemId);
	}
	
	@Override
	public List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req) {
		//需要前端同步的userid
		List<Long> uidList = getNeedSyncUidList(req.getReqList());
		//加载用户信息
		Map<Long, SummeryInfoDTO> batch = userSummaryCache.getBatch(uidList);
		
		return req.getReqList().stream().map(a -> batch.containsKey(a.getUid()) ? batch.get(a.getUid()) : SummeryInfoDTO.skip(a.getUid()))
				.filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	@Override
	public List<ItemInfoDTO> getItemInfo(ItemInfoReq req) { //更新时间可判断被修改
		return req.getReqList().stream().map(a -> {
			ItemConfig itemConfig = itemCache.getById(a.getItemId());
			if (Objects.nonNull(a.getLastModifyTime()) && a.getLastModifyTime() >= itemConfig.getUpdateTime().getTime()) {
				return ItemInfoDTO.skip(a.getItemId());
			}
			ItemInfoDTO dto = new ItemInfoDTO();
			dto.setItemId(itemConfig.getId());
			dto.setImg(itemConfig.getImg());
			dto.setDescribe(itemConfig.getDescribe());
			return dto;
		}).collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public void modifyName(Long uid, ModifyNameReq req) {
		//判断名字是不是重复
		String newName = req.getName();
		AssertUtil.isFalse(sensitiveWordBs.hasSensitiveWord(newName), "名字含有敏感词,请重新输入");
		User oldUser = userDao.getByName(newName);
		AssertUtil.isEmpty(oldUser, "名字已经被抢占了，请换一个哦~~");
		//判断改名卡够不够
		UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
		AssertUtil.isNotEmpty(firstValidItem, "改名卡不足，请先去商店购买");
		//使用改名卡
		boolean useSuccess = userBackpackDao.invalidItem(firstValidItem.getId());
		//用乐观锁，就不用分布式锁了
		if (useSuccess) {
			userDao.modifyName(uid, req.getName());
			//删除缓存
			userCache.userInfoChange(uid);
		}
	}
	
	@Override
	public List<BadgeResp> badges(Long uid) {
		List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
		////查询用户拥有的徽章
		List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
		//查询用户当前佩戴的标签
		User user = userDao.getById(uid);
		return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);
		
	}
	
	/**
	 * @param user
	 */
	@Override
	public void register(User user) {
		userDao.save(user);
		applicationEventPublisher.publishEvent(new UserRegisterEvent(this, user));
	}
	
	/**
	 * @param uid
	 * @param req
	 */
	@Override
	public void wearingBadge(Long uid, WearingBadgeReq req) {
		//确认是否有徽章
		UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, req.getBadgeId());
		AssertUtil.isNotEmpty(firstValidItem, "您没有这个徽章哦，快去达成条件获取吧");
		//确认物品类型是徽章
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItem.getItemId());
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "该徽章不可佩戴");
        //佩戴徽章
        userDao.wearingBadge(uid,req.getBadgeId());
        //删除用户缓存
        userCache.userInfoChange(uid);
    }
    
    /**
     * @param req 
     */
    @Override
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black black = new Black();
        black.setTarget(uid.toString());
        black.setType(BlackTypeEnum.UID.getType());
        blackDao.save(black);
        User user = userDao.getById(uid);
        blackIp(user.getIpInfo().getCreateIp());
        blackIp(user.getIpInfo().getUpdateIp());
        applicationEventPublisher.publishEvent(new UserBlackEvent(this, user));
    }
    
    private void blackIp(String updateIp) {
        if (StringUtils.isBlank(updateIp)) {
            return;
        }
        try {
            Black black = new Black();
            black.setType(BlackTypeEnum.IP.getType());
            black.setTarget(updateIp);
            blackDao.save(black);
            
        } catch (Exception e) {
            //错误处理
            log.error("duplicate black ip:{}", updateIp);
        }
    }
    
    private List<Long> getNeedSyncUidList(List<SummeryInfoReq.infoReq> reqList) {
		ArrayList<Long> needSyncUidList = new ArrayList<>();
		List<Long> userModifyTime = userCache.getUserModifyTime(reqList.stream().map(SummeryInfoReq.infoReq::getUid).collect(Collectors.toList()));
		for (int i = 0; i < reqList.size(); i++) {
			SummeryInfoReq.infoReq infoReq = reqList.get(i);
			Long modifyTime = userModifyTime.get(i);
			if (Objects.isNull(infoReq.getLastModifyTime()) || (Objects.nonNull(modifyTime) && modifyTime > infoReq.getLastModifyTime())) {
				needSyncUidList.add(infoReq.getUid());
			}
		}
		return needSyncUidList;
	}
}
