package com.yuankj.mallchat.chat.domain.vo.request.chat;

import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadReq extends CursorPageBaseReq {
    @ApiModelProperty("消息id")
    @NotNull
    private Long msgId;

    @ApiModelProperty("查询类型 1已读 2未读")
    @NotNull
    private Long searchType;
}