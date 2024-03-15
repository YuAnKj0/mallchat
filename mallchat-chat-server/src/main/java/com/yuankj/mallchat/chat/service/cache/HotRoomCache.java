package com.yuankj.mallchat.chat.service.cache;

import cn.hutool.core.lang.Pair;
import com.yuankj.mallchat.common.constant.RedisKey;
import com.yuankj.mallchat.common.domain.vo.request.CursorPageBaseReq;
import com.yuankj.mallchat.common.domain.vo.response.CursorPageBaseResp;
import com.yuankj.mallchat.common.utils.CursorUtils;
import com.yuankj.mallchat.common.utils.RedisUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * @author Ykj
 * @date 2024-03-08/15:09
 * @apiNote
 */
@Component
public class HotRoomCache {
	
	/*获取热门群聊翻页*/
	public CursorPageBaseResp<Pair<Long, Double>> getRoomCursorPage(CursorPageBaseReq pageBaseReq) {
		return CursorUtils.getCursorPageByRedis(pageBaseReq, RedisKey.getKey(RedisKey.HOT_ROOM_ZET), Long::parseLong);
	}
	public Set<ZSetOperations.TypedTuple<String>> getRoomRange(Double hotStart, Double hotEnd) {
		return RedisUtils.zRangeByScoreWithScores(RedisKey.getKey(RedisKey.HOT_ROOM_ZET), hotStart, hotEnd);
	}
	
	/**
	 * 更新热门群聊的最新时间
	 */
	public void refreshActiveTime(Long roomId, Date refreshTime) {
		RedisUtils.zAdd(RedisKey.getKey(RedisKey.HOT_ROOM_ZET), roomId, (double) refreshTime.getTime());
	}
	
	
	
}
