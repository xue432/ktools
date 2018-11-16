
CREATE TABLE `traffic_statistics` (
  `ip` varchar(15) NOT NULL COMMENT '访客IP(主键)',
  `gegraphic_pos` varchar(255) NOT NULL COMMENT '访客地理位置',
  `page_visit_times` int(11) NOT NULL DEFAULT '0' COMMENT '页面访问总次数',
  `api_visit_times` int(11) NOT NULL DEFAULT '0' COMMENT 'api访问总次数',
  PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站流量统计表';

CREATE TABLE `traffic_records` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` varchar(15) NOT NULL COMMENT '访客IP',
  `mac` varchar(155) NOT NULL DEFAULT '' COMMENT '访客物理地址',
  `isp` varchar(15) NOT NULL COMMENT '访客网络',
  `gegraphic_pos` varchar(255) NOT NULL COMMENT '访客地理位置',
  `req_url` varchar(255) NOT NULL COMMENT '访客请求url',
  `req_url_type` tinyint(1) NOT NULL COMMENT '请求url类型：0-页面 1-api',
  `req_method` varchar(100) NOT NULL COMMENT '请求方法',
  `req_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '请求状态：0-成功 1-失败',
  `req_time` int(6) NOT NULL DEFAULT '0' COMMENT '请求耗时(ms)',
  `req_type` varchar(10) NOT NULL COMMENT '请求方式',
  `req_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站流量日志记录表';