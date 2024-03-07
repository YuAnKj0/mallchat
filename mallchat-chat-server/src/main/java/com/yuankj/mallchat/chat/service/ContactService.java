package com.yuankj.mallchat.chat.service;

import com.yuankj.mallchat.chat.domain.dto.MsgReadInfoDTO;
import com.yuankj.mallchat.chat.domain.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * @author Ykj
 * @date 2024-02-26/17:53
 * @apiNote 会话列表服务类
 */

public interface ContactService {
	Map<Long, MsgReadInfoDTO> getMsgReadInfo(List<Message> messages);
}
