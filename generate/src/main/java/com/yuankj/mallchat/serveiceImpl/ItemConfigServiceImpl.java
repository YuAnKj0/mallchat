package com.yuankj.mallchat.serveiceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuankj.mallchat.mapper.ItemConfigMapper;
import com.yuankj.mallchat.model.ItemConfig;
import com.yuankj.mallchat.service.ItemConfigService;
/**
 * @author Ykj
 * @date 2023/10/8 0008/18:16
 * @apiNote 
 */
 
  
@Service
public class ItemConfigServiceImpl implements ItemConfigService{

    @Resource
    private ItemConfigMapper itemConfigMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return itemConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ItemConfig record) {
        return itemConfigMapper.insert(record);
    }

    @Override
    public int insertSelective(ItemConfig record) {
        return itemConfigMapper.insertSelective(record);
    }

    @Override
    public ItemConfig selectByPrimaryKey(Long id) {
        return itemConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ItemConfig record) {
        return itemConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ItemConfig record) {
        return itemConfigMapper.updateByPrimaryKey(record);
    }

}
