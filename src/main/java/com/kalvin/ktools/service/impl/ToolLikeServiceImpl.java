package com.kalvin.ktools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.ktools.dao.ToolLikeDao;
import com.kalvin.ktools.entity.ToolLike;
import com.kalvin.ktools.service.MenuService;
import com.kalvin.ktools.service.ToolLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 工具点赞统计表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
@Service
public class ToolLikeServiceImpl extends ServiceImpl<ToolLikeDao, ToolLike> implements ToolLikeService {

    @Autowired
    private MenuService menuService;

    @Override
    @Transactional
    public void giveLike(Long userId, Long menuId) {
        ToolLike toolLike = this.getByUMId(userId, menuId);
        if (toolLike == null) {
            toolLike = new ToolLike(userId, menuId, 1);
        } else {
            toolLike.setLikeIt(1);
        }
        super.saveOrUpdate(toolLike);
        // 喜欢+1
        menuService.upLikeNum(menuId);

    }

    @Override
    @Transactional
    public void dislike(Long userId, Long menuId) {
        ToolLike toolLike = this.getByUMId(userId, menuId);
        if (toolLike == null) {
            toolLike = new ToolLike(userId, menuId, 0);
        } else {
            toolLike.setLikeIt(0);
        }
        super.saveOrUpdate(toolLike);
        // 喜欢-1
        menuService.downLikeNum(menuId);
    }

    @Override
    public ToolLike getByUMId(Long userId, Long menuId) {
        return super.getOne(new QueryWrapper<ToolLike>().lambda().eq(ToolLike::getUserId, userId).eq(ToolLike::getMenuId, menuId));
    }
}
