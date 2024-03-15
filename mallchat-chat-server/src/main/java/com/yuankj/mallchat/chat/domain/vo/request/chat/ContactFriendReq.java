package com.yuankj.mallchat.chat.domain.vo.request.chat;

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
public class ContactFriendReq {

    @NotNull
    @ApiModelProperty("好友uid")
    private Long uid;
}