CREATE TABLE `kt_comment` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `menu_id` bigint(18) NOT NULL COMMENT '菜单ID',
  `comment` varchar(800) COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `comment_like_num` int(6) NOT NULL DEFAULT '0' COMMENT '当前评论喜欢数',
  `like` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否喜欢当前工具：0-未喜欢 1-喜欢',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工具评论表';