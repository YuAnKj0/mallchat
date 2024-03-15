package com.yuankj.mallchat.chat.controller;

import com.yuankj.mallchat.chat.domain.vo.request.chat.ContactFriendReq;
import com.yuankj.mallchat.chat.domain.vo.response.ChatRoomResp;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.chat.service.RoomAppService;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.request.IdReqVO;
import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/capi/chat")
@Api(tags = "聊天室相关接口")
@Slf4j
public class ContactController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private RoomAppService roomService;

    @GetMapping("/public/contact/page")
    @ApiOperation("会话列表")
    public ApiResult<CursorPageBaseResp<ChatRoomResp>> getRoomPage(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getContactPage(request, uid));
    }

    @GetMapping("/public/contact/detail")
    @ApiOperation("会话详情")
    public ApiResult<ChatRoomResp> getContactDetail(@Valid IdReqVO request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getContactDetail(uid, request.getId()));
    }

    @GetMapping("/public/contact/detail/friend")
    @ApiOperation("会话详情(联系人列表发消息用)")
    public ApiResult<ChatRoomResp> getContactDetailByFriend(@Valid ContactFriendReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getContactDetailByFriend(uid, request.getUid()));
    }
}