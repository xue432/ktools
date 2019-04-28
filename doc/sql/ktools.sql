/*
 Navicat Premium Data Transfer

 Source Server         : tencentCloud
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 119.29.193.127:3306
 Source Schema         : ktools

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 28/04/2019 20:25:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for kt_cmd_category
-- ----------------------------
DROP TABLE IF EXISTS `kt_cmd_category`;
CREATE TABLE `kt_cmd_category`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'linux命令名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'linux命令分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_comment
-- ----------------------------
DROP TABLE IF EXISTS `kt_comment`;
CREATE TABLE `kt_comment`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `menu_id` bigint(18) NOT NULL COMMENT '菜单ID',
  `comment` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `comment_like_num` int(6) NOT NULL DEFAULT 0 COMMENT '当前评论喜欢数',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工具评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_comment_give_detail
-- ----------------------------
DROP TABLE IF EXISTS `kt_comment_give_detail`;
CREATE TABLE `kt_comment_give_detail`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `comment_id` bigint(18) NOT NULL COMMENT '评论ID',
  `give_like_user_id` bigint(18) NOT NULL COMMENT '点赞用户ID',
  `like_it` tinyint(2) NOT NULL COMMENT '评论是否点赞：0-未点赞； 1-已点赞',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论点赞明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_linux_cmd
-- ----------------------------
DROP TABLE IF EXISTS `kt_linux_cmd`;
CREATE TABLE `kt_linux_cmd`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cmd_category_id` bigint(18) NOT NULL COMMENT '命令分类ID',
  `cmd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'linux命令',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'linux命令名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'linux命令表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_station_12306
-- ----------------------------
DROP TABLE IF EXISTS `kt_station_12306`;
CREATE TABLE `kt_station_12306`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '开头字母',
  `spell_s12306` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '12306缩写大写字母标识',
  `name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '站点名称',
  `all_spell` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '站点名称小写全拼',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2849 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '12306站点名称' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_ticket12306_order
-- ----------------------------
DROP TABLE IF EXISTS `kt_ticket12306_order`;
CREATE TABLE `kt_ticket12306_order`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT 'user12306表ID',
  `train_date` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '乘客出发日期，多个使用英文逗号分隔',
  `train_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车次，多个使用英文逗号分隔',
  `from_station` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '始发站',
  `to_station` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '到达站',
  `seat_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '座席类型。M：一等座；O：二等座；N：无座。多个用英文逗号分隔',
  `ticket_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '抢票状态。0：已停止；1：进行中',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '订单状态：0：正常；1：已取消',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '12306抢票订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_tool_like
-- ----------------------------
DROP TABLE IF EXISTS `kt_tool_like`;
CREATE TABLE `kt_tool_like`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `menu_id` bigint(18) NOT NULL COMMENT '菜单ID',
  `like_it` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否喜欢当前工具：0-未喜欢 1-喜欢',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工具点赞统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_traffic_records
-- ----------------------------
DROP TABLE IF EXISTS `kt_traffic_records`;
CREATE TABLE `kt_traffic_records`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客IP',
  `isp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客网络',
  `gegraphic_pos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客地理位置',
  `req_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客请求url',
  `req_url_type` tinyint(1) NOT NULL COMMENT '请求url类型：0-页面 1-api',
  `req_method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方法',
  `req_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '请求状态：0-成功 1-失败',
  `req_time` int(6) NOT NULL DEFAULT 0 COMMENT '请求耗时(ms)',
  `req_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `req_datetime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `mac` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '访客物理地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15853 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网站流量日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_traffic_statistics
-- ----------------------------
DROP TABLE IF EXISTS `kt_traffic_statistics`;
CREATE TABLE `kt_traffic_statistics`  (
  `ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客IP(主键)',
  `user_id` bigint(18) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `gegraphic_pos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客地理位置',
  `page_visit_times` int(11) NOT NULL DEFAULT 0 COMMENT '页面访问总次数',
  `api_visit_times` int(11) NOT NULL DEFAULT 0 COMMENT 'api访问总次数',
  `first_visit_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '第一次访问时间',
  PRIMARY KEY (`ip`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网站流量统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kt_user12306
-- ----------------------------
DROP TABLE IF EXISTS `kt_user12306`;
CREATE TABLE `kt_user12306`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '12306账号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '12306账号密码',
  `passenger_id_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '乘客身份证号',
  `passenger` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '乘客姓名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `ticket_num` int(6) NOT NULL DEFAULT 0 COMMENT '成功出票次数',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `12306账号`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '12306账号信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(18) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(18) NOT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单URL',
  `level` tinyint(2) NOT NULL DEFAULT 0 COMMENT '层级。0：模块；1：一级菜单；2：二级菜单(暂无)',
  `module_type` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块类型',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `banner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单牌面图片',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介',
  `like_num` int(6) NOT NULL DEFAULT 0 COMMENT '菜单工具喜欢数',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序值。越小越靠前',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态。0：正常；1：禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `qq` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
  `sex` tinyint(2) NOT NULL DEFAULT 0 COMMENT '性别：0-未知；1-男；2-女',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '用户类型：0-游客；1-系统注册用户',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
