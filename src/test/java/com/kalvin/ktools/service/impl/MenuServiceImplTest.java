package com.kalvin.ktools.service.impl;

import cn.hutool.json.JSONArray;
import com.kalvin.ktools.BaseTest;
import com.kalvin.ktools.service.MenuService;
import org.junit.Test;

import javax.annotation.Resource;


public class MenuServiceImplTest extends BaseTest {

    @Resource
    private MenuService menuService;

    @Test
    public void getAllMenuHierarchy() {
        JSONArray json = menuService.getAllMenuHierarchy();
        LOGGER.info("json={}", json.toString());
    }
}