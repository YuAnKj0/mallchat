package com.yuankj.mallchat.chat.service.cache;

import com.yuankj.mallchat.chat.dao.MessageDao;
import com.yuankj.mallchat.chat.domain.entity.Message;
import com.yuankj.mallchat.user.dao.BlackDao;
import com.yuankj.mallchat.user.dao.RoleDao;
import com.yuankj.mallchat.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MsgCache {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BlackDao blackDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MessageDao messageDao;

    @Cacheable(cacheNames = "msg", key = "'msg'+#msgId")
    public Message getMsg(Long msgId) {
        return messageDao.getById(msgId);
    }

    @CacheEvict(cacheNames = "msg", key = "'msg'+#msgId")
    public Message evictMsg(Long msgId) {
        return null;
    }
}