package com.kalvin.ktools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.dao.TrafficRecordsDao;
import com.kalvin.ktools.entity.Menu;
import com.kalvin.ktools.entity.TrafficRecords;
import com.kalvin.ktools.service.MenuService;
import com.kalvin.ktools.service.TrafficRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站流量日志记录表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
@Service
public class TrafficRecordsServiceImpl extends ServiceImpl<TrafficRecordsDao, TrafficRecords> implements TrafficRecordsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TrafficRecordsServiceImpl.class);

    @Resource
    private MenuService menuService;

    @Cacheable(value = Constant.CACHE_NAME)
    @Override
    public Map<String, Integer> countToolsView() {
        LOGGER.info("调用了缓存方法countToolsView");
        Map<String, Integer> countMap = new HashMap<>();
        List<Map> maps = baseMapper.selectToolsCount();

        maps.forEach(map -> {
            LambdaQueryWrapper<Menu> lambda = new QueryWrapper<Menu>().lambda();
            lambda.ne(Menu::getParentId, 0).eq(Menu::getStatus, Constant.STATUS_0);
            Menu menu = menuService.getOne(lambda.in(Menu::getUrl, map.get("reqUrl")));

            if (menu != null) {
                countMap.put(menu.getId().toString(), ((Long) map.get("count")).intValue());
            }
        });

        return countMap;
    }
}
