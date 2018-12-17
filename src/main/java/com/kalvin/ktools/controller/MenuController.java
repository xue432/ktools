package com.kalvin.ktools.controller;


import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-21
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    /**
     * 所有层级菜单数据接口
     * 第一次访问需要加到缓存中
     * @return R
     */
    @GetMapping(value = "all")
//    @Cacheable(value = Constant.CACHE_NAME, key = "\"allMenus\"")
    public R allHierarchy() {
        return R.ok(menuService.getAllMenuHierarchy());
    }

}

