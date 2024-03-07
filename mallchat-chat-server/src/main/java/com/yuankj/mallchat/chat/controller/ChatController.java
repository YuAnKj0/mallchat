package com.yuankj.mallchat.chat.controller;

import com.yuankj.mallchat.chat.domain.dto.MsgReadInfoDTO;
import com.yuankj.mallchat.chat.domain.vo.request.chat.*;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageReadResp;
import com.yuankj.mallchat.chat.service.ChatService;
import com.yuankj.mallchat.common.annocation.FrequencyControl;
import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMessageResp;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.RequestHolder;
import com.yuankj.mallchat.user.domain.enums.BlackTypeEnum;
import com.yuankj.mallchat.user.service.cache.UserCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ykj
 * @date 2023/10/8 0008/21:18
 * @apiNote
 */

@RestController
@RequestMapping("/capi/chat")
@Api("聊天室相关接口")
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private  ChatService chatService;
    @Autowired
    private  UserCache userCache;
    
    @GetMapping("/public/msg")
    @ApiOperation("消息列表")
    public ApiResult<CursorPageBaseResp<ChatMessageResp>> getMsgPage(@Valid ChatMessagePageReq request){
        CursorPageBaseResp<ChatMessageResp> msgPage=chatService.getMsgPage(request, RequestHolder.get().getUid());
        filterBlackMsg(msgPage);
        return ApiResult.success(msgPage);
    }
    
    @PostMapping("/msg")
    @ApiOperation("发送消息")
    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
    @FrequencyControl(time = 30, count = 5, target = FrequencyControl.Target.UID)
    @FrequencyControl(time = 60, count = 10, target = FrequencyControl.Target.UID)
    public ApiResult<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq request) {
        Long msgId = chatService.sendMsg(request, RequestHolder.get().getUid());
        //返回完整消息格式，方便前端展示
        return ApiResult.success(chatService.getMsgResp(msgId,RequestHolder.get().getUid()));
    }
    
    @PutMapping("/msg/mark")
    @ApiOperation("消息标记")
    @FrequencyControl(time = 10, count = 5, target = FrequencyControl.Target.UID)
    public ApiResult<Void> setMsgMark(@Valid @RequestBody ChatMessageMarkReq request){
        chatService.setMsgMark(request, RequestHolder.get().getUid());
        return ApiResult.success();
    }
    
    @PutMapping("/msg/recall")
    @ApiOperation("撤回消息")
    @FrequencyControl(time = 20, count = 3, target = FrequencyControl.Target.UID)
    public ApiResult<Void> recallMsg(@Valid @RequestBody ChatMessageBaseReq request){
        chatService.recallMsg(request, RequestHolder.get().getUid());
        return ApiResult.success();
    }
    
    @GetMapping("/msg/read/page")
    @ApiOperation("消息的已读未读列表")
    public ApiResult<CursorPageBaseResp<ChatMessageReadResp>> getReadPage(@Valid ChatMessageReadReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(chatService.getReadPage(uid, request));
    }
    @GetMapping("/msg/read")
    @ApiOperation("获取消息的已读未读总数")
    public ApiResult<Collection<MsgReadInfoDTO>> getReadInfo(@Valid ChatMessageReadInfoReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(chatService.getMsgReadInfo(uid, request));
    }
    @PutMapping("/msg/read")
    @ApiOperation("消息阅读上报")
    public ApiResult<Void> msgRead(@Valid @RequestBody ChatMessageMemberReq request) {
        Long uid = RequestHolder.get().getUid();
        chatService.msgRead(uid, request);
        return ApiResult.success();
    }
    
    
    
    
    private void filterBlackMsg(CursorPageBaseResp<ChatMessageResp> memberPage) {
        Set<String> blackMembers=getBlackUidSet();
        memberPage.getList().removeIf(a->blackMembers.contains(a.getFromUser().getUid().toString()));
        
    }
    
    private Set<String> getBlackUidSet() {
        return userCache.getBlackMap().getOrDefault(BlackTypeEnum.UID.getType(),new HashSet<>());
       
    }
    
    
}
