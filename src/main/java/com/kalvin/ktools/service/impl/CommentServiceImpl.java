package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.entity.Comment;
import com.kalvin.ktools.dao.CommentDao;
import com.kalvin.ktools.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工具评论表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

}
