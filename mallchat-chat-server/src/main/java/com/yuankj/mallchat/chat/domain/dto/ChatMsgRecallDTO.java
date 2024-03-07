package com.yuankj.mallchat.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ykj
 * @date 2024/02/26
 * @apiNote 消息撤回的推送类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgRecallDTO {
    private Long msgId;
    private Long roomId;
    //撤回的用户
    private Long recallUid;
}