package com.kalvin.ktools.service;

import com.kalvin.ktools.entity.ToolLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 工具点赞统计表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
public interface ToolLikeService extends IService<ToolLike> {

    /**
     * 点赞/喜欢
     * @param userId 用户ID
     * @param menuId 菜单ID
     */
    void giveLike(Long userId, Long menuId);

    /**
     * 取消喜欢
     * @param userId 用户ID
     * @param menuId 菜单ID
     */
    void dislike(Long userId, Long menuId);

    /**
     * 通过用户及菜单ID获取
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return
     */
    ToolLike getByUMId(Long userId, Long menuId);

}
