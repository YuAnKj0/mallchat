package com.yuankj.mallchat.chat.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.enums.MessageStatusEnum;
import com.yuankj.mallchat.chat.domain.vo.request.chat.ChatMessagePageReq;
import com.yuankj.mallchat.chat.mapper.MessageMapper;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Ykj
 * @date 2023/10/9 0009/18:30
 * @apiNote
 */
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {
	public CursorPageBaseResp<Message> getCursorPage(Long roomId, ChatMessagePageReq request, Long lastMsgId) {
		return CursorUtils.getCursorPageByMysql(this,request,wrapper->{
			wrapper.eq(Message::getRoomId,roomId);
			wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
			wrapper.le(Objects.nonNull(lastMsgId),Message::getId,lastMsgId);
		},Message::getId);
	}
}
