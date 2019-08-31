package com.kalvin.ktools.controller;

import com.kalvin.ktools.comm.annotation.SiteStats;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页控制层
 */
@RestController
@RequestMapping(value = "/")
public class IndexController {

    @Autowired
    private MenuService menuService;

    @SiteStats
    @GetMapping(value = "")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @SiteStats
    @GetMapping(value = "about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }

    @SiteStats
    @GetMapping(value = "search")
    public ModelAndView search() {
        return new ModelAndView("search");
    }

    @GetMapping(value = "test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }

    @GetMapping(value = "search/tool")
    public R searchTool(String word) {
        return R.ok(menuService.listMenuByName(word));
    }
}
