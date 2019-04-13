package com.kalvin.ktools.dto;

import com.kalvin.ktools.entity.Comment;

/**
 * 评论数据实体 带nickname
 */
public class CommentDTO extends Comment {

    private String nickname;
    private String avatar;
    private int likeIt;

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

    public int getLikeIt() {
        return likeIt;
    }

    public void setLikeIt(int likeIt) {
        this.likeIt = likeIt;
    }
}
