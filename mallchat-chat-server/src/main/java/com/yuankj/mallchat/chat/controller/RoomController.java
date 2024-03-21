package com.yuankj.mallchat.chat.controller;

import com.yuankj.mallchat.chat.domain.vo.request.chat.ChatMessageMemberReq;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberDelReq;
import com.yuankj.mallchat.chat.domain.vo.request.member.MemberReq;
import com.yuankj.mallchat.chat.domain.vo.response.ChatMemberListResp;
import com.yuankj.mallchat.chat.domain.vo.response.MemberResp;
import com.yuankj.mallchat.chat.service.IGroupMemberService;
import com.yuankj.mallchat.chat.service.RoomAppService;
import com.yuankj.mallchat.common.domain.vo.request.IdReqVO;
import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.RequestHolder;
import com.yuankj.mallchat.user.domain.vo.response.ws.ChatMemberResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-20/10:04
 * @apiNote
 */
@RestController
@RequestMapping("/capi/room")
@Api(tags = "聊天室相关接口")
@Slf4j
public class RoomController {
	@Autowired
	private RoomAppService roomService;
	@Autowired
	private IGroupMemberService groupMemberService;
	
	@GetMapping("/public/group")
	@ApiOperation("群组详情")
	public ApiResult<MemberResp> groupDetail(@Valid IdReqVO request) {
		Long uid = RequestHolder.get().getUid();
		return ApiResult.success(roomService.getGroupDetail(uid, request.getId()));
	}
	
	@GetMapping("/public/group/member/page")
	@ApiOperation("群成员列表")
	public ApiResult<CursorPageBaseResp<ChatMemberResp>> getMemberPage(@Valid MemberReq request) {
		return ApiResult.success(roomService.getMemberPage(request));
	}
	
	@GetMapping("/group/member/list")
	@ApiOperation("房间内的所有群成员列表-@专用")
	public ApiResult<List<ChatMemberListResp>> getMemberList(@Valid ChatMessageMemberReq request) {
		return ApiResult.success(roomService.getMemberList(request));
	}
	@DeleteMapping("/group/member")
	@ApiOperation("移除成员")
	public ApiResult<Void> delMember(@Valid @RequestBody MemberDelReq request) {
		Long uid = RequestHolder.get().getUid();
		roomService.delMember(uid, request);
		return ApiResult.success();
	}
  
}
