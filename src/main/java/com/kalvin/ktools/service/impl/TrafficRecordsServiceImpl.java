package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.entity.TrafficRecords;
import com.kalvin.ktools.dao.TrafficRecordsDao;
import com.kalvin.ktools.service.TrafficRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
