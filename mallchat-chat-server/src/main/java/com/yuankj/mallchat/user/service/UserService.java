package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.user.domain.dao.SummeryInfoDTO;
import com.yuankj.mallchat.user.domain.dto.ItemInfoDTO;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.vo.request.user.*;
import com.yuankj.mallchat.user.domain.vo.response.user.BadgeResp;
import com.yuankj.mallchat.user.domain.vo.response.user.UserInfoResp;

import java.util.List;

/**
 * @author Ykj
 * @date 2023-10-18/14:25
 * @apiNote
 */

public interface UserService {
    /**
     * @param uid
     * @return {@code UserInfoResp}
     */
    UserInfoResp getUserInfo(Long uid);
    
    /**
     * @param req
     * @return {@code List<SummeryInfoDTO>}
     */
    List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req);
    
    /**
     * @param req
     * @return {@code List<ItemInfoDTO>}
     */
    List<ItemInfoDTO> getItemInfo(ItemInfoReq req);
    
    /**
     * @param uid
     * @param req
     */
    void modifyName(Long uid, ModifyNameReq req);
    
    /**
     * 用户徽章
     *
     * @param uid
     * @return {@code List<BadgeResp>}
     */
    List<BadgeResp> badges(Long uid);
    
    void register(User user);
    
    void wearingBadge(Long uid, WearingBadgeReq req);
    
    void black(BlackReq req);
}
