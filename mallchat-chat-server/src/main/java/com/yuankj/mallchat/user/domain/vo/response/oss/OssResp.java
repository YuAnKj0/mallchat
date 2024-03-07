package com.yuankj.mallchat.user.domain.vo.response.oss;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ykj
 * @date 2024-03-06/14:46
 * @apiNote Oss请求出参
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OssResp {
	
	@ApiModelProperty(value = "上传的临时url")
	private String uploadUrl;
	
	@ApiModelProperty(value = "成功后能够下载的url")
	private String downloadUrl;
}
