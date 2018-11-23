package com.kalvin.ktools.service;

import cn.hutool.json.JSONArray;
import com.kalvin.ktools.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
