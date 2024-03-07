package com.yuankj.mallchat.user.domain.vo.request.oss;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Ykj
 * @date 2024-03-06/14:43
 * @apiNote 上传url请求入参
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadUrlReq {
	@ApiModelProperty(value = "文件名（带后缀）")
	@NotBlank
	private String fileName;
	@ApiModelProperty(value = "上传场景1.聊天室,2.表情包")
	@NotNull
	private Integer scene;
}
