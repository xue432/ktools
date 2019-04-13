package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 评论点赞明细表
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-13
 */
@TableName("kt_comment_give_detail")
public class CommentGiveDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 点赞用户ID
     */
    private Long giveLikeUserId;

    /**
     * 评论是否点赞：0-未点赞； 1-已点赞
     */
    private Integer likeIt;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public CommentGiveDetail() {

    }

    public CommentGiveDetail(Long commentId, Long giveLikeUserId, Integer likeIt) {
        this.commentId = commentId;
        this.giveLikeUserId = giveLikeUserId;
        this.likeIt = likeIt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getGiveLikeUserId() {
        return giveLikeUserId;
    }

    public void setGiveLikeUserId(Long giveLikeUserId) {
        this.giveLikeUserId = giveLikeUserId;
    }

    public Integer getLikeIt() {
        return likeIt;
    }

    public void setLikeIt(Integer likeIt) {
        this.likeIt = likeIt;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CommentGiveDetail{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", giveLikeUserId=" + giveLikeUserId +
                ", likeIt=" + likeIt +
                ", createTime=" + createTime +
                '}';
    }
}
