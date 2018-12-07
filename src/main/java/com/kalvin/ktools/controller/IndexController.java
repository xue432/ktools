package com.kalvin.ktools.controller;

import cn.hutool.core.util.StrUtil;
import com.kalvin.ktools.comm.annotation.SiteStats;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.exception.KTException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页控制层
 */
@RestController
@RequestMapping(value = "/")
public class IndexController {

    @SiteStats
    @GetMapping(value = "")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @SiteStats
    @GetMapping(value = "get/data")
    public R getData(String key) {
        if (StrUtil.isEmpty(key)) {
            throw new KTException("key is null");
        }
        return R.ok(key);
    }

    @GetMapping(value = "test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }
}
