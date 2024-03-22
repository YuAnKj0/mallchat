package com.yuankj.mallchat.chat.dao;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.enums.MessageStatusEnum;
import com.yuankj.mallchat.chat.domain.vo.request.chat.ChatMessagePageReq;
import com.yuankj.mallchat.chat.mapper.MessageMapper;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
	
	public Integer getUnReadCount(Long roomId, Date readTime) {
		return lambdaQuery()
				.eq(Message::getRoomId, roomId)
				.gt(Objects.nonNull(readTime), Message::getCreateTime, readTime)
				.count();
	}
	
	/**
	 * 根据房间ID逻辑删除消息
	 * @param roomId
	 * @param uidList
	 * @return
	 */
	public Boolean removeByRoomId(Long roomId, List<Long> uidList) {
		if (CollectionUtil.isNotEmpty(uidList)) {
			LambdaUpdateWrapper<Message> wrapper = new UpdateWrapper<Message>().lambda()
					.eq(Message::getRoomId, roomId)
					.in(Message::getFromUid, uidList)
					.set(Message::getStatus, MessageStatusEnum.DELETE.getStatus());
			return this.update(wrapper);
		}
		return false;
	}
}
