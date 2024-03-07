package com.yuankj.mallchat.user.controller;

import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.common.utils.RequestHolder;
import com.yuankj.mallchat.user.domain.dao.SummeryInfoDTO;
import com.yuankj.mallchat.user.domain.dto.ItemInfoDTO;
import com.yuankj.mallchat.user.domain.enums.RoleEnum;
import com.yuankj.mallchat.user.domain.vo.request.user.*;
import com.yuankj.mallchat.user.domain.vo.response.user.BadgeResp;
import com.yuankj.mallchat.user.domain.vo.response.user.UserInfoResp;
import com.yuankj.mallchat.user.service.IRoleService;
import com.yuankj.mallchat.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ykj
 * @date 2023-10-18/14:20
 * @apiNote
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户管理相关接口")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private IRoleService iRoleService;
	
	
	@GetMapping("/userInfo")
	@ApiOperation("用户详情")
	public ApiResult<UserInfoResp> getUserInfo() {
		
		return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
		
	}
	
	@PostMapping("/public/summary/userInfo/batch")
	@ApiOperation("用户聚合信息-返回的代表需要刷新的")
	public ApiResult<List<SummeryInfoDTO>> getSummeryUserInfo(@Valid @RequestBody SummeryInfoReq req) {
		
		return ApiResult.success(userService.getSummeryUserInfo(req));
	}
	
	@PostMapping("/public/badges/batch")
	@ApiOperation("徽章聚合信息-返回的代表需要刷新的")
	public ApiResult<List<ItemInfoDTO>> getItemInfo(@Valid @RequestBody ItemInfoReq req) {
		return ApiResult.success(userService.getItemInfo(req));
	}
	
	@PutMapping("/name")
	@ApiOperation("修改用户名")
	public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
		userService.modifyName(RequestHolder.get().getUid(), req);
		return ApiResult.success();
	}
	
	@GetMapping("/badges")
	@ApiOperation("可选徽章预览")
	public ApiResult<List<BadgeResp>> badges() {
		return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
	}
	
	@PutMapping("/badge")
	@ApiOperation("佩戴徽章")
	public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq req) {
		userService.wearingBadge(RequestHolder.get().getUid(), req);
		return ApiResult.success();
		
	}
	
	@PutMapping("/black")
	@ApiOperation("拉黑用户")
	public ApiResult<Void> black(@Valid @RequestBody BlackReq req) {
		Long uid = RequestHolder.get().getUid();
		boolean hasPower = iRoleService.hasPower(uid, RoleEnum.ADMIN);
		AssertUtil.isTrue(hasPower, "没有权限");
		userService.black(req);
		return ApiResult.success();
	}
	
}
