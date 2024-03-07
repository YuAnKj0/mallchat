package com.yuankj.mallchat.chat.domain.entity.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoundMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("大小（字节）")
    @NotNull
    private Long size;

    @ApiModelProperty("时长（秒）")
    @NotNull
    private Integer second;

    @ApiModelProperty("下载地址")
    @NotBlank
    private String url;
}