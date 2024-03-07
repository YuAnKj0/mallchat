package com.yuankj.mallchat.chat.service.impl;

import com.yuankj.mallchat.chat.dao.ContactDao;
import com.yuankj.mallchat.chat.domain.dto.MsgReadInfoDTO;
import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.service.ContactService;
import com.yuankj.mallchat.common.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-02-26/17:55
 * @apiNote
 */

@Service
public class ContactServiceImpl implements ContactService {
	
	@Autowired
	private ContactDao contactDao;
	
	@Override
	public Map<Long, MsgReadInfoDTO> getMsgReadInfo(List<Message> messages) {
		Map<Long, List<Message>> roomGroup = messages.stream().collect(Collectors.groupingBy(Message::getRoomId));
		AssertUtil.equal(roomGroup.size(), 1, "只能查询同一房间的消息");
		Long roomId = roomGroup.keySet().iterator().next();
		Integer totalCount = contactDao.getTotalCount(roomId);
		return messages.stream().map(message ->{
			MsgReadInfoDTO msgReadInfoDTO = new MsgReadInfoDTO();
			msgReadInfoDTO.setMsgId(message.getId());
			Integer readCount = contactDao.getReadCount(message);
			msgReadInfoDTO.setReadCount(readCount);
			msgReadInfoDTO.setUnReadCount(totalCount - readCount-1);
			return msgReadInfoDTO;
		}).collect(Collectors.toMap(MsgReadInfoDTO::getMsgId, Function.identity()));
	}
}
