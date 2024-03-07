package com.yuankj.mallchat.user.controller;

import com.yuankj.mallchat.common.domain.vo.response.ApiResult;
import com.yuankj.mallchat.common.utils.RequestHolder;
import com.yuankj.mallchat.user.domain.vo.request.oss.UploadUrlReq;
import com.yuankj.mallchat.user.domain.vo.response.oss.OssResp;
import com.yuankj.mallchat.user.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * oss控制层
 */

@RestController
@RequestMapping("/capi/oss")
@Api(tags = "oss相关接口")
public class OssController {
    @Autowired
    private OssService ossService;

    @GetMapping("/upload/url")
    @ApiOperation("获取临时上传链接")
    public ApiResult<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return ApiResult.success(ossService.getUploadUrl(RequestHolder.get().getUid(), req));
    }
}