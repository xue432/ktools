package com.kalvin.ktools;

import cn.hutool.extra.mail.MailUtil;
import com.kalvin.ktools.comm.kit.Shakedown12306Kit;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作用：<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年01月23日 16:35
 */
public class MainTest2 {

    public static void main(String[] args) {
        String trainDate = "2019-01-28";
        String fromStation = "IZQ";
        String toStation = "FAQ";
        String trainNum = "D2985,D2959,D4707,D4285,D2951,G2901,D2809,D2367,D1801,D1867";
        trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
        String seats = "M,O,N";
        Shakedown12306Kit
                .newInstance()
                .initUser("18819458084", "llytest123")
                .initQueryInfo(trainDate, fromStation, toStation, trainNum, seats)
                .run();
    }
}
