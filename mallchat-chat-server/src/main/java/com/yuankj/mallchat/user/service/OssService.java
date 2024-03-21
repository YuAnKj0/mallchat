package com.yuankj.mallchat.user.service;

import com.yuankj.mallchat.domain.OssResp;
import com.yuankj.mallchat.user.domain.vo.request.oss.UploadUrlReq;

public interface OssService {

    /**
     * 获取临时的上传链接
     */
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}