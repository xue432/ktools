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
     * 菜单ID
     */
    private Long menuId;

    /**
     * 工具喜欢数
     */
    private Integer likeNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    @Override
    public String toString() {
        return "ToolLike{" +
        "id=" + id +
        ", menuId=" + menuId +
        ", likeNum=" + likeNum +
        "}";
    }
}
