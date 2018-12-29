package com.kalvin.ktools.controller;


import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.service.TrafficRecordsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 网站流量日志记录表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
@RestController
@RequestMapping("/trafficRecords")
public class TrafficRecordsController {

    @Resource
    private TrafficRecordsService trafficRecordsService;

    @GetMapping(value = "count/toolsView")
    public R countToolsView() {
        return R.ok(trafficRecordsService.countToolsView());
    }

}

