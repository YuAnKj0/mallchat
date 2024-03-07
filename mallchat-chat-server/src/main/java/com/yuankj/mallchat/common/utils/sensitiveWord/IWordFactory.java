package com.yuankj.mallchat.common.utils.sensitiveWord;

import java.util.List;



public interface IWordFactory {
    /**
     * 返回敏感词数据源
     *
     * @return 结果
     * @since 0.0.13
     */
    List<String> getWordList();
}