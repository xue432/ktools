package com.kalvin.ktools.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页控制层
 */
@RestController
@RequestMapping(value = "index")
public class IndexController {

    @GetMapping(value = "")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping(value = "get/data")
    public String getData() {
        return "{code:200,msg:'请求成功'}";
    }

    @GetMapping(value = "test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }
}
