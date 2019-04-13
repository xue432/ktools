package com.kalvin.ktools.service;

import com.kalvin.ktools.entity.CommentGiveDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论点赞明细表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-13
 */
public interface CommentGiveDetailService extends IService<CommentGiveDetail> {

    /**
     * 根据评论及用户ID获取点赞明细
     * @param commentId 评论ID
     * @param giveLikeUserId 用户ID
     * @return
     */
    CommentGiveDetail getByCUId(Long commentId, Long giveLikeUserId);

}
