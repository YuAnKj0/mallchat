package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.user.domain.vo.request.oss.UploadUrlReq;
import com.yuankj.mallchat.user.domain.vo.response.oss.OssResp;

public interface OssService {

    /**
     * 获取临时的上传链接
     */
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}