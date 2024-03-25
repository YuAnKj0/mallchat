package com.yuankj.mallchat.user.service.cache;


import cn.hutool.core.collection.CollUtil;
import com.yuankj.mallchat.common.constant.RedisKey;
import com.yuankj.mallchat.common.utils.RedisUtils;
import com.yuankj.mallchat.user.dao.BlackDao;
import com.yuankj.mallchat.user.dao.RoleDao;
import com.yuankj.mallchat.user.dao.UserDao;
import com.yuankj.mallchat.user.dao.UserRoleDao;
import com.yuankj.mallchat.user.domain.entity.Black;
import com.yuankj.mallchat.user.domain.entity.User;
import com.yuankj.mallchat.user.domain.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ykj
 * @date 2023/10/8 0008/22:57
 * @apiNote
 */
@Component
@RequiredArgsConstructor
public class UserCache {
    
    @Autowired
    private UserDao userDao;
    @Autowired
    private BlackDao blackDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;
    
    private final UserSummaryCache userSummaryCache;
   
    @Cacheable(cacheNames = "user",key="'blackList'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> result = new HashMap<>(collect.size());
        for (Map.Entry<Integer, List<Black>> entry : collect.entrySet()) {
            result.put(entry.getKey(), entry.getValue().stream().map(Black::getTarget).collect(Collectors.toSet()));
        }
        return result;
    }
    
    /**
     * 获取用户信息
     * //TODO 后期做二级缓存
     * @param uid
     * @return
     */
    public User getUserInfo(Long uid) {
        return getUserInfoBatch(Collections.singleton(uid)).get(uid);
    }
    
    public Map<Long, User> getUserInfoBatch(Set<Long> uids) {
        //批量组装key
        List<String> keys = uids.stream().map(a -> RedisKey.getKey(RedisKey.USER_INFO_STRING, a)).collect(Collectors.toList());
        //批量get
        List<User> mget = RedisUtils.mget(keys, User.class);
        Map<Long, User> map = mget.stream().filter(Objects::nonNull).collect(Collectors.toMap(User::getId, Function.identity()));
        //发现差集——还需要load更新的uid
        List<Long> needLoadUidList = uids.stream().filter(a -> !map.containsKey(a)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needLoadUidList)) {
            //批量load
            List<User> needLoadUserList = userDao.listByIds(needLoadUidList);
            Map<String, User> redisMap = needLoadUserList.stream().collect(Collectors.toMap(a -> RedisKey.getKey(RedisKey.USER_INFO_STRING, a.getId()), Function.identity()));
            RedisUtils.mset(redisMap, 5 * 60);
            //加载回redis
            map.putAll(needLoadUserList.stream().collect(Collectors.toMap(User::getId, Function.identity())));
        }
        return map;
        
    }
    
    public List<Long> getUserModifyTime(List<Long> uidList) {
        List<String> keys =uidList.stream().map(uid->RedisKey.getKey(RedisKey.USER_MODIFY_STRING,uid)).collect(Collectors.toList());
        return RedisUtils.mget(keys,Long.class);        
    }
    
    public void userInfoChange(Long uid) {
        delUserInfo(uid);
        //删除UserSummaryCache，前端下次懒加载的时候可以获取到最新的数据
        userSummaryCache.delete(uid);
        refreshUserModifyTime(uid);
    }
    
    private void refreshUserModifyTime(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid);
        RedisUtils.set(key, System.currentTimeMillis() / 1000);
    }
    
    private void delUserInfo(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
        RedisUtils.del(key);
    }
    
    //Cacheable注解：将方法的运行结果进行缓存，使得后续相同的请求可以直接从缓存中获取数据，减少了对数据库等的访问频率，
    //`cacheNames` 或 `value`：用来指定缓存组件的名字，可以将方法的返回结果放置在指定的缓存中，可以通过数组方式指定多个缓存。1234
    //`key`：缓存数据的键，通常由方法参数生成，也可以通过`keyGenerator`指定自定义的Key生成器。
    @Cacheable(cacheNames = "user", key = "'roles'+#uid")
	public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    }
	
	public Long getOnlineNum() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return RedisUtils.zCard(onlineKey);
	}
}

