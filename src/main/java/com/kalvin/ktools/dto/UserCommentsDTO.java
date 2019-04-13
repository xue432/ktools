package com.kalvin.ktools.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.ktools.entity.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * 用户评论 数据
 */
public class UserCommentsDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 是否喜欢
     */
    private int likeIt;
    /**
     * 喜欢人数
     */
    private int likeNum;

    /**
     * 评论列表
     */
    private Page<CommentDTO> comments;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public int getLikeIt() {
        return likeIt;
    }

    public void setLikeIt(int likeIt) {
        this.likeIt = likeIt;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public Page<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(Page<CommentDTO> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "UserCommentsDTO{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", menuId=" + menuId +
                ", likeIt=" + likeIt +
                ", likeNum=" + likeNum +
                ", comments=" + comments +
                '}';
    }
}
