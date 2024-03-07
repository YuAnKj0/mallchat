package com.yuankj.mallchat.common.utils.discover.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ykj
 * @date 2023/10/9 0009/18:43
 * @apiNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlInfo {
    /**
     * 标题
     **/
    String title;
    
    /**
     * 描述
     **/
    String description;
    
    /**
     * 网站LOGO
     **/
    String image;
    
}
