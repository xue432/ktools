
CREATE TABLE `kt_comment` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `menu_id` bigint(18) NOT NULL COMMENT '菜单ID',
  `comment` varchar(800) COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `comment_like_num` int(6) NOT NULL DEFAULT '0' COMMENT '当前评论喜欢数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工具评论表';


CREATE TABLE `kt_tool_like` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `menu_id` bigint(18) NOT NULL COMMENT '菜单ID',
  `like_it` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否喜欢当前工具：0-未喜欢 1-喜欢',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工具点赞统计表';


CREATE TABLE `kt_comment_give_detail` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `comment_id` bigint(18) NOT NULL COMMENT '评论ID',
  `give_like_user_id` bigint(18) NOT NULL COMMENT '点赞用户ID',
  `like_it` tinyint(2) NOT NULL COMMENT '评论是否点赞：0-未点赞； 1-已点赞',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='评论点赞明细表';