CREATE TABLE `kt_tool_like` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` bigint(18) NOT NULL COMMENT '菜单ID',
  `like_num` int(6) NOT NULL DEFAULT '0' COMMENT '工具喜欢数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工具点赞统计表';