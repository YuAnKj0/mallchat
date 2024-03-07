package com.yuankj.mallchat.user.domain.vo.response.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FriendCheckResp {
  
  @ApiModelProperty("校验结果")
  private List<FriendCheck> checkedList;
  
  @Data
  public static class FriendCheck {
    private Long uid;
    private Boolean isFriend;
  }
  
}
