package com.yuankj.mallchat.chat.controller;

import com.yuankj.mallchat.chat.domain.vo.response.MemberResp;
import com.yuankj.mallchat.chat.service.IGroupMemberService;
import com.yuankj.mallchat.chat.service.RoomAppService;
import com.yuankj.mallchat.common.domain.vo.request.IdReqVO;
import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
	
  
  
}
