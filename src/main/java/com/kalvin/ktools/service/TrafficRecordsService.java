package com.kalvin.ktools.service;

import com.kalvin.ktools.entity.TrafficRecords;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站流量日志记录表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
public interface TrafficRecordsService extends IService<TrafficRecords> {

    /**
     * 统计所有工具被查看的次数，统计数据封装为map对象
     * @return map
     */
    Map<String, Integer> countToolsView();

}
