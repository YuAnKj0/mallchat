package com.yuankj.mallchat.chatai.service;

import com.yuankj.mallchat.chat.domain.entity.Message;

public interface IChatAIService {

    void chat(Message message);
}