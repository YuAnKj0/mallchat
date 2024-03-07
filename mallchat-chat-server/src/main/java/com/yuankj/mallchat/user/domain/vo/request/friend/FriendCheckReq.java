package com.yuankj.mallchat.user.domain.vo.request.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-06/15:41
 * @apiNote
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckReq {
	
	@NotEmpty
	@Size(max = 50)
	@ApiModelProperty("校验好友的uid")
	private List<Long> uidList;
	
}