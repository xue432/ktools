package com.kalvin.ktools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kalvin.ktools.entity.TrafficRecords;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站流量日志记录表 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
public interface TrafficRecordsDao extends BaseMapper<TrafficRecords> {

    /**
     * 查询所有工具被查看过的次数
     * @return list
     */
    @Select("SELECT any_value(req_url) reqUrl,count(req_method) AS count FROM kt_traffic_records " +
            "WHERE req_url_type = 0  AND req_method != 'index' GROUP BY req_method")
    List<Map> selectToolsCount();

}
