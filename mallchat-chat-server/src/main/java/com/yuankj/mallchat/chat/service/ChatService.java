package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.dto.MsgReadInfoDTO;
import com.yuankj.mallchat.chat.domain.vo.request.*;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageReadResp;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;

import java.util.Collection;

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
	
	ChatMessageResp getMsgResp(Long msgId, Long uid);
	
	void setMsgMark(ChatMessageMarkReq request, Long uid);
	
	void recallMsg(ChatMessageBaseReq request, Long uid);
	
	CursorPageBaseResp<ChatMessageReadResp> getReadPage(Long uid, ChatMessageReadReq request);
	
	Collection<MsgReadInfoDTO> getMsgReadInfo(Long uid, ChatMessageReadInfoReq request);
	
	void msgRead(Long uid, ChatMessageMemberReq request);
}
