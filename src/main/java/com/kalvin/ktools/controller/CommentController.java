package com.kalvin.ktools.controller;


import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.entity.Comment;
import com.kalvin.ktools.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工具评论表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

   /**
     * 用户评论数据
     * @param comment 评论实体参数
     * @return r
     */
    @GetMapping(value = "userComments")
    public R userComments(Comment comment) {
        return R.ok(commentService.getUserComments(comment));
    }

    /**
     * 新增评论
     * @param comment 评论实体参数
     * @return r
     */
    @PostMapping(value = "add")
    public R addComment(Comment comment) {
        Comment rc = commentService.addComment(comment);
        return R.ok(rc.getId());
    }

    /**
     * 点赞/喜欢
     * @param id 评论ID
     * @param giveLikeUserId 用户ID
     * @return
     */
    @PostMapping(value = "giveLike")
    public R giveLike(Long id, Long giveLikeUserId) {
        commentService.giveLike(id, giveLikeUserId);
        return R.ok();
    }

    /**
     * 取消点赞/喜欢
     * @param id 评论ID
     * @param giveLikeUserId 用户ID
     * @return
     */
    @PostMapping(value = "dislike")
    public R dislike(Long id, Long giveLikeUserId) {
        commentService.dislike(id, giveLikeUserId);
        return R.ok();
    }

}

