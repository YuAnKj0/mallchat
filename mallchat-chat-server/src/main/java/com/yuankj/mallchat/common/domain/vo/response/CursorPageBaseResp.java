package com.yuankj.mallchat.common.domain.vo.response;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ykj
 * @date 2023/10/8 0008/22:36
 * @apiNote
 */
@Data
@ApiModel("游标翻页返回")
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseResp<T> {
    
    @ApiModelProperty("游标（下次翻页带上这参数）")
    private String cursor;
    
    @ApiModelProperty("是否最后一页")
    private Boolean isLast = Boolean.FALSE;
    
    @ApiModelProperty("数据列表")
    private List<T> list;
    
    public static <T> CursorPageBaseResp<T> init(CursorPageBaseResp cursorPage, List<T> list){
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp.setIsLast(cursorPage.getIsLast());
        cursorPageBaseResp.setCursor(cursorPage.getCursor());
        cursorPageBaseResp.setList(list);
        return cursorPageBaseResp;
        
    }
    
    @JsonIgnore
    public Boolean isEmpty() {
        return CollectionUtil.isEmpty(list);
    }
    
    public static <T> CursorPageBaseResp<T> empty() {
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<T>();
        cursorPageBaseResp.setIsLast(true);
        cursorPageBaseResp.setList(new ArrayList<T>());
        return cursorPageBaseResp;
    }
}
