package com.kalvin.ktools.controller;


import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.service.ToolLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工具点赞统计表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
@RestController
@RequestMapping("/toolLike")
public class ToolLikeController {

    @Autowired
    private ToolLikeService toolLikeService;

    /**
     * 点赞/喜欢
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return
     */
    @PostMapping(value = "giveLike")
    public R giveLike(Long userId, Long menuId) {
        toolLikeService.giveLike(userId, menuId);
        return R.ok();
    }

    /**
     * 取消点赞/喜欢
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return
     */
    @PostMapping(value = "dislike")
    public R dislike(Long userId, Long menuId) {
        toolLikeService.dislike(userId, menuId);
        return R.ok();
    }

}

