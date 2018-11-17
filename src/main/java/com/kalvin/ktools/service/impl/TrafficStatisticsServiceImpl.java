package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.entity.TrafficStatistics;
import com.kalvin.ktools.dao.TrafficStatisticsDao;
import com.kalvin.ktools.service.TrafficStatisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网站流量统计表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
@Service
public class TrafficStatisticsServiceImpl extends ServiceImpl<TrafficStatisticsDao, TrafficStatistics> implements TrafficStatisticsService {

}
