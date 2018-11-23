
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

-- 初始化菜单表数据
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (1, 0, '图片相关', '/image/artPic', 0, 'IMAGE', '', '', 0, 0, '2018-11-21 16:43:55');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (2, 1, '图片转换艺术图', '/image/artPic', 1, NULL, '', 'static/image/ktpysj.png', 0, 0, '2018-11-21 16:46:54');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (3, 0, '文字相关', '/txt/ascii', 0, 'TXT', '', '', 0, 0, '2018-11-21 16:48:05');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (4, 1, '图片转换字符画', '/image/asciiPic', 1, NULL, '', 'static/image/ktzfh.png', 0, 0, '2018-11-22 17:41:15');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (5, 1, 'GIF转换AsciiGif', '/image/asciiGif', 1, NULL, '', 'static/image/ascgif.gif', 0, 0, '2018-11-22 17:42:15');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (6, 1, '图片转换彩色Ascii图', '/image/colorAsciiPic', 1, NULL, '', 'static/image/clascpic.png', 0, 0, '2018-11-22 17:42:41');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (7, 1, '图片合成GIF动图', '/image/gif', 1, NULL, '', 'static/image/cpgif.gif', 0, 0, '2018-11-22 17:43:13');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (8, 3, '文字转换ascii艺术字', '/txt/ascii', 1, NULL, ' ', 'static/image/ktysj.png', 0, 0, '2018-11-22 17:43:57');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (9, 0, '头像相关', '/head', 0, 'HEAD', '', '', 0, 0, '2018-11-22 17:44:32');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (10, 9, '头像大全', '/head', 1, NULL, '', '', 0, 0, '2018-11-22 17:45:39');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (11, 9, '生成头像', '/head/pixel', 1, NULL, '', '', 0, 0, '2018-11-22 17:46:04');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (12, 0, '便民工具', '', 0, 'CONVENIENCE', '', '', 0, 0, '2018-11-22 17:48:59');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (13, 0, '开发者工具', '', 0, 'DEV', '', '', 0, 0, '2018-11-22 17:49:22');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (14, 0, '其它工具', '', 0, 'OTHER', '', '', 0, 0, '2018-11-22 17:50:23');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (15, 14, '开发中', '', 1, NULL, '', '', 0, 0, '2018-11-22 17:50:56');
