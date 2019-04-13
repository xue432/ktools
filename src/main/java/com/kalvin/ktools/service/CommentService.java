package com.kalvin.ktools.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.ktools.dto.CommentDTO;
import com.kalvin.ktools.dto.UserCommentsDTO;
import com.kalvin.ktools.entity.Comment;

/**
 * <p>
 * 工具评论表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
public interface CommentService extends IService<Comment> {

    /**
     * 根据菜单ID获取对应工具用户评论数据
     * @param comment 评论实体参数
     * @return
     */
    UserCommentsDTO getUserComments(Comment comment);

    /**
     * 获取用户评论 分页
     * @param comment 评论实体参数
     * @return
     */
    Page<CommentDTO> getCommentsByPage(Comment comment);

    /**
     * 发表评论
     * @param comment 评论实体参数
     * @return comment
     */
    Comment addComment(Comment comment);

    /**
     * 点赞当条评论
     * @param id 评论ID
     * @param giveLikeUserId 点赞用户ID
     */
    void giveLike(Long id, Long giveLikeUserId);

    /**
     * 取消点赞当条评论
     * @param id 评论ID
     * @param giveLikeUserId 点赞用户ID
     */
    void dislike(Long id, Long giveLikeUserId);

    /**
     * 喜欢/点赞数 +1
     * @param id 评论ID
     */
    void upLikeNum(Long id);

    /**
     * 喜欢/点赞数 -1
     * @param id 评论ID
     */
    void downLikeNum(Long id);

}
