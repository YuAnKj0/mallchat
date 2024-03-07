package com.yuankj.mallchat.user.service.impl;

import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.domain.vo.response.IdRespVO;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.user.dao.UserEmojiDao;
import com.yuankj.mallchat.user.domain.entity.UserEmoji;
import com.yuankj.mallchat.user.domain.vo.request.user.UserEmojiReq;
import com.yuankj.mallchat.user.domain.vo.response.user.UserEmojiResp;
import com.yuankj.mallchat.user.service.UserEmojiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-02-29/17:33
 * @apiNote
 */
@Service
@Slf4j
public class UserEmojiServiceImpl implements UserEmojiService {
	@Autowired
	private UserEmojiDao userEmojiDao;
	
	/**
	 * @param uid
	 * @return
	 */
	@Override
	public List<UserEmojiResp> list(Long uid) {
		return userEmojiDao.listByUid(uid).stream().map(a -> UserEmojiResp.builder()
				.id(a.getId()).expressionUrl(a.getExpressionUrl()).build()).collect(Collectors.toList());
	}
	
	/**
	 * @param req 
	 * @param uid
	 * @return
	 */
	@Override
	public ApiResult<IdRespVO> insert(UserEmojiReq req, Long uid) {
		//校验表情数量是否超过30
		int count = userEmojiDao.countByUid(uid);
		AssertUtil.isFalse(count>30,"最多只能添加30个表情");
		//检验表情包是否存在
		Integer existsCount = userEmojiDao.lambdaQuery().eq(UserEmoji::getExpressionUrl, req.getExpressionUrl())
				.eq(UserEmoji::getUid, uid).count();
		AssertUtil.isFalse(existsCount>0,"表情包已存在");
		UserEmoji userEmoji = UserEmoji.builder().uid(uid).expressionUrl(req.getExpressionUrl()).build();
		userEmojiDao.save(userEmoji);
		return ApiResult.success(IdRespVO.id(userEmoji.getId()));
		
	}
	
	/**
	 * @param id 
	 * @param uid
	 */
	@Override
	public void remove(long id, Long uid) {
		UserEmoji userEmoji = userEmojiDao.getById(id);
		AssertUtil.isNotEmpty(userEmoji, "表情不能为空");
		AssertUtil.equal(userEmoji.getUid(), uid, "小黑子，别人表情不是你能删的");
		userEmojiDao.removeById(id);
		
	}
	
	
}
