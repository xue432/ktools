package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.BaseTest;
import com.kalvin.ktools.entity.LinuxCmd;
import com.kalvin.ktools.service.LinuxCmdService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class LinuxCmdServiceImplTest extends BaseTest {

    @Resource
    private LinuxCmdService linuxCmdService;

    @Test
    public void getByKeyword() {
        List<LinuxCmd> list = linuxCmdService.getByKeyword("取消按预定时间关闭系统");
        list.forEach(System.out::println);
    }

}