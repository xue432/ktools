package com.kalvin.ktools.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.dao.MenuDao;
import com.kalvin.ktools.entity.Menu;
import com.kalvin.ktools.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Cacheable(value = Constant.CACHE_NAME, key = "\"allMenus\"")
    @Override
    public JSONArray getAllMenuHierarchy() {
        LOGGER.info("调用了缓存方法getAllHierarchy");
        List<Menu> menus = baseMapper.selectList(
                new QueryWrapper<Menu>().eq("parent_id", 0)
                        .eq("status", Constant.STATUS_0));
        JSONArray jsonArray = new JSONArray();
        menus.forEach(menu -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("module", menu.getName());
            jsonObject.put("moduleType", menu.getModuleType());
            jsonObject.put("url", menu.getUrl());
            jsonObject.put("menu", recurQuery(menu));
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }

    /**
     * 递归获取所有可用子菜单
     * @param menu 菜单
     * @return jsonArr
     */
    private JSONArray recurQuery(Menu menu) {
        List<Menu> menus = baseMapper.selectList(
                new QueryWrapper<Menu>().eq("parent_id", menu.getId())
                        .eq("status", Constant.STATUS_0));
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtil.isNotEmpty(menus)) {
            menus.forEach(menuCl -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", menuCl.getId());
                jsonObject.put("name", menuCl.getName());
                jsonObject.put("url", menuCl.getUrl());
                jsonObject.put("icon", StrUtil.isNotEmpty(menuCl.getIcon())
                        ? menuCl.getIcon() : Constant.DEFAULT_ICON);
                jsonObject.put("banner", StrUtil.isNotEmpty(menuCl.getBanner())
                        ? menuCl.getBanner() : Constant.DEFAULT_BANNER);
                jsonObject.put("level", menuCl.getLevel());
                jsonObject.put("menu", recurQuery(menuCl));
                jsonObject.put("intro", menuCl.getIntro());
                jsonArray.add(jsonObject);
            });
        }
        return jsonArray;
    }
}
