package com.kalvin.ktools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kalvin.ktools.entity.CommentGiveDetail;
import com.kalvin.ktools.dao.CommentGiveDetailDao;
import com.kalvin.ktools.service.CommentGiveDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论点赞明细表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-13
 */
@Service
public class CommentGiveDetailServiceImpl extends ServiceImpl<CommentGiveDetailDao, CommentGiveDetail> implements CommentGiveDetailService {

    @Override
    public CommentGiveDetail getByCUId(Long commentId, Long giveLikeUserId) {
        return super.getOne(new QueryWrapper<CommentGiveDetail>().lambda()
                .eq(CommentGiveDetail::getCommentId, commentId)
                .eq(CommentGiveDetail::getGiveLikeUserId, giveLikeUserId));
    }
}
