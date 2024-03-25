package com.yuankj.mallchat.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuankj.mallchat.chat.domain.entity.WxMsg;
import com.yuankj.mallchat.chat.mapper.WxMsgMapper;
import org.springframework.stereotype.Service;

@Service
public class WxMsgDao extends ServiceImpl<WxMsgMapper, WxMsg> {

}