package dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import domain.entity.SecureInvokeRecord;
import mapper.SecureInvokeRecordMapper;
import org.springframework.stereotype.Component;
import service.SecureInvokeService;

import java.util.Date;
import java.util.List;

/**
 * @author Ykj
 * @date 2024-03-18/14:53
 * @apiNote
 */
@Component
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {
	
	public List<SecureInvokeRecord> getWaitRetryRecords() {
		Date now = new Date();
		//查2分钟前的失败数据。避免刚入库的数据被查出来
		DateTime afterTime = DateUtil.offsetMinute(now, -(int) SecureInvokeService.RETRY_INTERVAL_MINUTES);
		return lambdaQuery()
				.eq(SecureInvokeRecord::getStatus, SecureInvokeRecord.STATUS_WAIT)
				.lt(SecureInvokeRecord::getNextRetryTime, new Date())
				.lt(SecureInvokeRecord::getCreateTime, afterTime)
				.list();
	}
}
