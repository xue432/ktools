
CREATE TABLE `kt_traffic_statistics` (
  `ip` varchar(15) NOT NULL COMMENT '访客IP(主键)',
  `gegraphic_pos` varchar(255) NOT NULL COMMENT '访客地理位置',
  `page_visit_times` int(11) NOT NULL DEFAULT '0' COMMENT '页面访问总次数',
  `api_visit_times` int(11) NOT NULL DEFAULT '0' COMMENT 'api访问总次数',
  `first_visit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '第一次访问时间',
  PRIMARY KEY (`ip`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='网站流量统计表';

CREATE TABLE `kt_traffic_records` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` varchar(15) NOT NULL COMMENT '访客IP',
  `isp` varchar(50) NOT NULL COMMENT '访客网络',
  `gegraphic_pos` varchar(255) NOT NULL COMMENT '访客地理位置',
  `req_url` varchar(255) NOT NULL COMMENT '访客请求url',
  `req_url_type` tinyint(1) NOT NULL COMMENT '请求url类型：0-页面 1-api',
  `req_method` varchar(100) NOT NULL COMMENT '请求方法',
  `req_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '请求状态：0-成功 1-失败',
  `req_time` int(6) NOT NULL DEFAULT '0' COMMENT '请求耗时(ms)',
  `req_type` varchar(10) NOT NULL COMMENT '请求方式',
  `req_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `mac` varchar(155) NOT NULL DEFAULT '' COMMENT '访客物理地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='网站流量日志记录表';

CREATE TABLE `sys_menu` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(18) NOT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) NOT NULL COMMENT '菜单名称',
  `url` varchar(200) NOT NULL COMMENT '菜单URL',
  `level` tinyint(2) NOT NULL DEFAULT '0' COMMENT '层级。0：模块；1：一级菜单；2：二级菜单(暂无)',
  `module_type` varchar(25) DEFAULT NULL COMMENT '模块类型',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `banner` varchar(50) NOT NULL COMMENT '菜单牌面图片',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值。越小越靠前',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态。0：正常；1：禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE `kt_cmd_category` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT 'linux命令名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='linux命令分类';


CREATE TABLE `kt_linux_cmd` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cmd_category_id` bigint(18) NOT NULL COMMENT '命令分类ID',
  `cmd` varchar(255) NOT NULL COMMENT 'linux命令',
  `name` varchar(150) NOT NULL COMMENT 'linux命令名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='linux命令表';

CREATE TABLE `kt_station_12306` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '开头字母',
  `spell_s12306` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '12306缩写大写字母标识',
  `name` varchar(25) NOT NULL COMMENT '站点名称',
  `all_spell` varchar(50) NOT NULL COMMENT '站点名称小写全拼',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2849 DEFAULT CHARSET=utf8mb4 COMMENT='12306站点名称';

CREATE TABLE `kt_user12306` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(150) COLLATE utf8mb4_general_ci NOT NULL COMMENT '12306账号',
  `password` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '12306账号密码',
  `passenger_id_no` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乘客身份证号',
  `passenger` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乘客姓名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱地址',
  `ticket_num` int(6) NOT NULL DEFAULT '0' COMMENT '成功出票次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `12306账号` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='12306账号信息表';

CREATE TABLE `kt_ticket12306_order` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT 'user12306表ID',
  `train_date` varchar(200) NOT NULL COMMENT '乘客出发日期，多个使用英文逗号分隔',
  `train_num` varchar(255) NOT NULL COMMENT '车次，多个使用英文逗号分隔',
  `from_station` varchar(15) NOT NULL COMMENT '始发站',
  `to_station` varchar(15) NOT NULL COMMENT '到达站',
  `seat_type` varchar(20) NOT NULL COMMENT '座席类型。M：一等座；O：二等座；N：无座。多个用英文逗号分隔',
  `ticket_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '抢票状态。0：已停止；1：进行中',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态：0：正常；1：已取消',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='12306抢票订单信息表';