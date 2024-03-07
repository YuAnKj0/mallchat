package com.yuankj.mallchat.chat.service.adapter;

import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.entity.MessageMark;
import com.yuankj.mallchat.chat.domain.enums.MessageMarkTypeEnum;
import com.yuankj.mallchat.chat.domain.enums.MessageStatusEnum;
import com.yuankj.mallchat.chat.domain.enums.MessageTypeEnum;
import com.yuankj.mallchat.chat.domain.vo.request.chat.ChatMessageReq;
import com.yuankj.mallchat.chat.domain.vo.request.msg.TextMsgReq;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.chat.service.strategy.msg.AbstractMsgHandler;
import com.yuankj.mallchat.chat.service.strategy.msg.MsgHandlerFactory;
import com.yuankj.mallchat.common.domain.enums.YesOrNoEnum;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-02-23/15:30
 * @apiNote 消息适配器
 */

public class MessageAdapter {
	
	public static Message buildMsgSave(ChatMessageReq request, Long uid) {
		
		return Message.builder().fromUid(uid).roomId(request.getRoomId()).type(request.getMsgType()).status(MessageStatusEnum.NORMAL.getStatus()).build();
		
	}
	
	public static List<ChatMessageResp> buildMsgSave(List<Message> messages, List<MessageMark> msgMark, Long receiveUid) {
		Map<Long, List<MessageMark>> markMap = msgMark.stream().collect(Collectors.groupingBy(MessageMark::getMsgId));
		return messages.stream().map(a -> {
					ChatMessageResp chatMessageResp = new ChatMessageResp();
					chatMessageResp.setMessage(buildMessage(a, markMap.getOrDefault(a.getId(), new ArrayList<>()), receiveUid));
					chatMessageResp.setFromUser(buildFromUser(a.getFromUid()));
					return chatMessageResp;
				})
				//帮前端排好序
				.sorted(Comparator.comparing(a -> a.getMessage().getSendTime()))
				.collect(Collectors.toList());
	}
	
	private static ChatMessageResp.UserInfo buildFromUser(Long fromUid) {
		ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
		userInfo.setUid(fromUid);
		return userInfo;
	}
	
	private static ChatMessageResp.Message buildMessage(Message message, List<MessageMark> marks, Long receiveUid) {
		ChatMessageResp.Message messageVO = new ChatMessageResp.Message();
		BeanUtils.copyProperties(message, messageVO);
		messageVO.setSendTime(message.getCreateTime());
		AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(message.getType());
		if (Objects.nonNull(msgHandler)) {
			messageVO.setBody(msgHandler.showMsg(message));
		}
		//消息标记
		messageVO.setMessageMark(buildMsgMark(marks, receiveUid));
		return messageVO;
	}
	
	private static ChatMessageResp.MessageMark buildMsgMark(List<MessageMark> marks, Long receiveUid) {
		Map<Integer, List<MessageMark>> typeMap = marks.stream().collect(Collectors.groupingBy(MessageMark::getType));
		
		List<MessageMark> likeMarks = typeMap.getOrDefault(MessageMarkTypeEnum.LIKE.getType(), new ArrayList<>());
		List<MessageMark> dislikeMarks = typeMap.getOrDefault(MessageMarkTypeEnum.DISLIKE.getType(), new ArrayList<>());
		ChatMessageResp.MessageMark mark = new ChatMessageResp.MessageMark();
		mark.setLikeCount(likeMarks.size());
		mark.setUserLike(Optional.ofNullable(receiveUid).filter(uid -> likeMarks.stream().anyMatch(a -> Objects.equals(a.getUid(), uid))).map(a -> YesOrNoEnum.YES.getStatus()).orElse(YesOrNoEnum.NO.getStatus()));
		mark.setDislikeCount(dislikeMarks.size());
		mark.setUserDislike(Optional.ofNullable(receiveUid).filter(uid -> dislikeMarks.stream().anyMatch(a -> Objects.equals(a.getUid(), uid))).map(a -> YesOrNoEnum.YES.getStatus()).orElse(YesOrNoEnum.NO.getStatus()));
		return mark;
		
	}
	
	public static ChatMessageReq buildAgreeMsg(Long roomId) {
		ChatMessageReq chatMessageReq = new ChatMessageReq();
		chatMessageReq.setRoomId(roomId);
		chatMessageReq.setMsgType(MessageTypeEnum.TEXT.getType());
		TextMsgReq textMsgReq = new TextMsgReq();
		textMsgReq.setContent("我们已经成为好友了，开始聊天吧");
		chatMessageReq.setBody(textMsgReq);
		return chatMessageReq;
	}
}
