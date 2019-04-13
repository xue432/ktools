package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 工具点赞统计表
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
@TableName("kt_tool_like")
public class ToolLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 是否喜欢当前工具：0-未喜欢 1-喜欢
     */
    private Integer likeIt;

    public ToolLike() {

    }

    public ToolLike(Long userId, Long menuId, Integer likeIt) {
        this.userId = userId;
        this.menuId = menuId;
        this.likeIt = likeIt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getLikeIt() {
        return likeIt;
    }

    public void setLikeIt(Integer likeIt) {
        this.likeIt = likeIt;
    }

    @Override
    public String toString() {
        return "ToolLike{" +
                "id=" + id +
                ", userId=" + userId +
                ", menuId=" + menuId +
                ", likeIt=" + likeIt +
                '}';
    }
}
