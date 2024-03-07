package com.yuankj.mallchat.chat.service.adapter;

import com.yuankj.mallchat.chat.domain.entity.Contact;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageReadResp;
import com.yuankj.mallchat.chat.domain.vo.response.ChatRoomResp;
import jodd.bean.BeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2024-02-26/17:16
 * @apiNote
 */

public class RoomAdapter {
	
	
	public static List<ChatMessageReadResp> buildReadResp(List<Contact> list) {
		return list.stream().map(contact -> {
			ChatMessageReadResp resp = new ChatMessageReadResp();
			resp.setUid(contact.getUid());
			return resp;
		}).collect(Collectors.toList());
	}
}
