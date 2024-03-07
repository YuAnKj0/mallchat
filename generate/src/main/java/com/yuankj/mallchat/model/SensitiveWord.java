package com.yuankj.mallchat.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
/**
    * 敏感词库
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveWord implements Serializable {
    /**
    * 敏感词
    */
    private String word;

    private static final long serialVersionUID = 1L;
}