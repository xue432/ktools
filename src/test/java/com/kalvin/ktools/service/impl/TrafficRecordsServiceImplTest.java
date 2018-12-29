package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.BaseTest;
import com.kalvin.ktools.service.TrafficRecordsService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * 作用：<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2018年12月28日 15:40
 */
public class TrafficRecordsServiceImplTest extends BaseTest {

    @Resource
    private TrafficRecordsService trafficRecordsService;

    @Test
    public void countToolsView() {
        trafficRecordsService.countToolsView();
    }
}