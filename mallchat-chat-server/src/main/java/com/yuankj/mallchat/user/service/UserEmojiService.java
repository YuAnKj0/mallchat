package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.domain.vo.response.IdRespVO;
import com.yuankj.mallchat.user.domain.vo.request.user.UserEmojiReq;
import com.yuankj.mallchat.user.domain.vo.response.user.UserEmojiResp;

import java.util.List;

/**
 * @author Ykj
 * @date 2024-02-29/17:32
 * @apiNote
 */

public interface UserEmojiService {
	List<UserEmojiResp> list(Long uid);
	
	ApiResult<IdRespVO> insert(UserEmojiReq req, Long uid);
	
	void remove(long id, Long uid);
}
