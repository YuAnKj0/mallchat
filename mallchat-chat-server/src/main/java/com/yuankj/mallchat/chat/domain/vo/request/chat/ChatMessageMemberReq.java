package com.yuankj.mallchat.chat.domain.vo.request.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @apiNote 消息列表请求
 * @author Ykj
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageMemberReq {
    @NotNull
    @ApiModelProperty("会话id")
    private Long roomId;
}