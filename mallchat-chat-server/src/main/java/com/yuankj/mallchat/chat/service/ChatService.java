package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.dto.MsgReadInfoDTO;
import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.vo.request.chat.*;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberReq;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageReadResp;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.user.domain.vo.response.ws.ChatMemberResp;

import java.util.Collection;
import java.util.List;

/**
 * @author Ykj
 * @date 2023/10/8 0008/21:20
 * @apiNote
 */

public interface ChatService {
    CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, Long uid);
	
	/**
	 * 发送消息
	 * @param request
	 * @param uid
	 * @return
	 */
	Long sendMsg(ChatMessageReq request, Long uid);
	
	ChatMessageResp getMsgResp(Long msgId, Long receiveUid);
	
	ChatMessageResp getMsgResp(Message message, Long receiveUid);
	
	void setMsgMark(ChatMessageMarkReq request, Long uid);
	
	void recallMsg(ChatMessageBaseReq request, Long uid);
	
	CursorPageBaseResp<ChatMessageReadResp> getReadPage(Long uid, ChatMessageReadReq request);
	
	Collection<MsgReadInfoDTO> getMsgReadInfo(Long uid, ChatMessageReadInfoReq request);
	
	void msgRead(Long uid, ChatMessageMemberReq request);
	
	CursorPageBaseResp<ChatMemberResp> getMemberPage(List<Long> memberUidList, MemberReq request);
}
