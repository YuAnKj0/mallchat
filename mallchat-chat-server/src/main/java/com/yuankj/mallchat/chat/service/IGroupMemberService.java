package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.vo.request.member.MemberExitReq;

/**
 * @author Ykj
 * @date 2024-03-20/10:18
 * @apiNote
 */

public interface IGroupMemberService {
	void exitGroup(Long uid, MemberExitReq request);
}
