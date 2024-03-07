package com.yuankj.mallchat.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.user.domain.entity.UserEmoji;
import com.yuankj.mallchat.user.mapper.UserEmojiMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ykj
 * @date 2024-02-29/17:34
 * @apiNote
 */

@Service
public class UserEmojiDao extends ServiceImpl<UserEmojiMapper, UserEmoji>{
	public List<UserEmoji> listByUid(Long uid) {
		return lambdaQuery().eq(UserEmoji::getUid, uid).list();
	}
	
	public int countByUid(Long uid) {
		return lambdaQuery().eq(UserEmoji::getUid, uid).count();
	}
	
}
