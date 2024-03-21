package com.yuankj.mallchat;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.yuankj.mallchat.domain.OssReq;

import com.yuankj.mallchat.domain.OssResp;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

/**
 * @author Ykj
 * @date 2024-03-06/15:04
 * @apiNote
 */
@Slf4j
@AllArgsConstructor
public class MinIOTemplate {
	
	/**
	 * MinIO 客户端
	 */
	MinioClient minioClient;
	
	/**
	 * MinIO 配置类
	 */
	OssProperties ossProperties;
	
	/**
	 * 返回临时带签名、过期时间一天、PUT请求方式的访问URL
	 *
	 * @param req
	 * @return
	 */
	@SneakyThrows
	public OssResp getPreSignedObjectUrl(OssReq req) {
		String absolutePath = req.isAutoPath() ? generateAutoPath(req) : req.getFilePath() + StrUtil.SLASH + req.getFileName();
		String url = minioClient.getPresignedObjectUrl(
				GetPresignedObjectUrlArgs.builder()
						.method(Method.PUT)
						.bucket(ossProperties.getBucketName())
						.object(absolutePath)
						.expiry(60 * 60 * 24)
						.build());
		return OssResp.builder().uploadUrl(url).downloadUrl(getDownloadUrl(ossProperties.getBucketName(), absolutePath)).build();
	}
	
	private String getDownloadUrl(String bucketName, String absolutePath) {
		return ossProperties.getEndpoint() + StrUtil.SLASH + bucketName + absolutePath;
	}
	
	/**
	 * 生成随机文件名，防止重复
	 *
	 * @param req
	 * @return
	 */
	public String generateAutoPath(OssReq req) {
		String uid = Optional.ofNullable(req.getUid()).map(String::valueOf).orElse("000000");
		UUID uuid = UUID.fastUUID();
		String suffix = FileNameUtil.getSuffix(req.getFileName());
		String yearAndMonth = DateUtil.format(new Date(), DatePattern.NORM_MONTH_PATTERN);
		return req.getFilePath() + StrUtil.SLASH + yearAndMonth + StrUtil.SLASH + uid + StrUtil.SLASH + uuid + StrUtil.DOT + suffix;
	}
}
