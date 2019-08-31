package com.kalvin.ktools.service;

import cn.hutool.json.JSONArray;
import com.kalvin.ktools.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-21
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有可用层级菜单
     * @return 菜单jsonArr
     */
    JSONArray getAllMenuHierarchy();

    /**
     * 喜欢+1
     * @param id 菜单ID
     */
    void upLikeNum(Long id);

    /**
     * 喜欢-1
     * @param id 菜单ID
     */
    void downLikeNum(Long id);

    List<Menu> listMenuByKeyword(String keyword);

}
