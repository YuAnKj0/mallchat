package com.yuankj.mallchat.chat.consumer;

import com.yuankj.mallchat.chat.dao.ContactDao;
import com.yuankj.mallchat.chat.dao.MessageDao;
import com.yuankj.mallchat.chat.dao.RoomDao;
import com.yuankj.mallchat.chat.dao.RoomFriendDao;
import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.chat.domain.entity.Room;
import com.yuankj.mallchat.chat.domain.entity.RoomFriend;
import com.yuankj.mallchat.chat.domain.enums.RoomTypeEnum;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.chat.service.WeChatMsgOperationService;
import com.yuankj.mallchat.chat.service.cache.GroupMemberCache;
import com.yuankj.mallchat.chat.service.cache.HotRoomCache;
import com.yuankj.mallchat.chat.service.cache.RoomCache;
import com.yuankj.mallchat.chatai.service.IChatAIService;
import com.yuankj.mallchat.common.constant.MQConstant;
import com.yuankj.mallchat.common.domain.dto.MsgSendMessageDTO;
import com.yuankj.mallchat.user.service.WebSocketService;
import com.yuankj.mallchat.user.service.adapter.WSAdapter;
import com.yuankj.mallchat.user.service.cache.UserCache;
import com.yuankj.mallchat.user.service.impl.PushService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Ykj
 * @date 2024-03-22/19:17
 * @apiNote
 */
@Component
@RocketMQMessageListener(consumerGroup = MQConstant.SEND_MSG_GROUP, topic = MQConstant.SEND_MSG_TOPIC)
public class MsgSendConsumer implements RocketMQListener<MsgSendMessageDTO> {
	
	@Autowired
	private WebSocketService webSocketService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private IChatAIService openAIService;
	@Autowired
	WeChatMsgOperationService weChatMsgOperationService;
	@Autowired
	private RoomCache roomCache;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private GroupMemberCache groupMemberCache;
	@Autowired
	private UserCache userCache;
	@Autowired
	private RoomFriendDao roomFriendDao;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private ContactDao contactDao;
	@Autowired
	private HotRoomCache hotRoomCache;
	@Autowired
	private PushService pushService;
	/**
	 * @param dto 
	 */
	@Override
	public void onMessage(MsgSendMessageDTO dto) {
		Message message = messageDao.getById(dto.getMsgId());
		Room room = roomCache.get(message.getRoomId());
		ChatMessageResp msgResp=chatService.getMsgResp(message, null);
		//所有房间更新房间最新消息
		roomDao.refreshActiveTime(room.getId(), message.getId(),message.getCreateTime());
		roomCache.delete(room.getId());
		if (room.isHotRoom()){
			//热门群聊推送所有在线的人
			//更新热门群聊时间-redis
			hotRoomCache.refreshActiveTime(room.getId(),message.getCreateTime());
			//推送所有人
			pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp));
		}else {
			List<Long> memberUidList = new ArrayList<>();
			if (Objects.equals(room.getType(), RoomTypeEnum.GROUP.getType())) {
				//普通群聊推送所有群成员
				memberUidList=groupMemberCache.getMemberUidList(room.getId());
			} else if (Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())){
				//对单人推送
				RoomFriend roomFriend=roomFriendDao.getByRoomId(room.getId());
				memberUidList= Arrays.asList(roomFriend.getUid1(),roomFriend.getUid2());
			}
			//更新所有群成员的会话时间
			contactDao.refreshOrCreateActiveTime(room.getId(), memberUidList, message.getId(), message.getCreateTime());
			//推送房间成员
			pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp), memberUidList);
		}
	}
}
