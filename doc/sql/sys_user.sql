CREATE TABLE `sys_user` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `password` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `qq` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'QQ号',
  `sex` tinyint(2) NOT NULL DEFAULT '0' COMMENT '性别：0-未知；1-男；2-女',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户类型：0-游客；1-系统注册用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';