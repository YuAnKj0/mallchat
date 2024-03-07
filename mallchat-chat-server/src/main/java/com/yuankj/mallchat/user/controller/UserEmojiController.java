package com.yuankj.mallchat.user.controller;

import com.yuankj.mallchat.common.domain.vo.request.IdReqVO;
import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.domain.vo.response.IdRespVO;
import com.yuankj.mallchat.common.utils.RequestHolder;
import com.yuankj.mallchat.user.domain.vo.request.user.UserEmojiReq;
import com.yuankj.mallchat.user.domain.vo.response.user.UserEmojiResp;
import com.yuankj.mallchat.user.service.UserEmojiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ykj
 * @date 2024-02-29/17:30
 * @apiNote  用户emoji相关接口
 */
@RestController
@RequestMapping("/capi/user/emoji")
@Api(tags = "用户表情包管理相关接口")
public class UserEmojiController {
	
	
	@Autowired
	private UserEmojiService emojiService;
	
	
	@GetMapping("/list")
	@ApiOperation("表情包列表")
	public ApiResult<List<UserEmojiResp>> getEmojisPage() {
		return ApiResult.success(emojiService.list(RequestHolder.get().getUid()));
	}
	
	@PostMapping()
	@ApiOperation("新增表情包")
	public ApiResult<IdRespVO> insertEmojis(@Valid @RequestBody UserEmojiReq req) {
		return emojiService.insert(req, RequestHolder.get().getUid());
	}
	
	@DeleteMapping()
	@ApiOperation("删除表情包")
	public ApiResult<Void> deleteEmojis(@Valid @RequestBody IdReqVO reqVO) {
		emojiService.remove(reqVO.getId(), RequestHolder.get().getUid());
		return ApiResult.success();
	}
	
}
