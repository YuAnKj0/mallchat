package com.yuankj.mallchat.user.service.impl;

import com.mallchat.oss.MinIOTemplate;
import com.mallchat.oss.domain.OssReq;
import com.mallchat.oss.domain.OssResp;
import com.yuankj.mallchat.common.utils.AssertUtil;
import com.yuankj.mallchat.user.domain.enums.OssSceneEnum;
import com.yuankj.mallchat.user.domain.vo.request.oss.UploadUrlReq;
import com.yuankj.mallchat.user.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ykj
 * @date 2024-03-06/14:47
 * @apiNote
 */

@Service
public class OssServiceImpl implements OssService {
	
	@Autowired
	private MinIOTemplate minIOTemplate;
	
	
	/**
	 * 获取临时的上传链接
	 *
	 * @param uid
	 * @param req
	 */
	@Override
	public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
		OssSceneEnum ossSceneEnum = OssSceneEnum.of(req.getScene());
		AssertUtil.isNotEmpty(ossSceneEnum,"场景有误");
		OssReq build = OssReq.builder()
				.fileName(req.getFileName())
				.filePath(ossSceneEnum.getPath())
				.uid(uid)
				.build();
		return minIOTemplate.getPreSignedObjectUrl(build);
		
	}
}
