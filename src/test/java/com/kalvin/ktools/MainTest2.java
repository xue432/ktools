package com.kalvin.ktools;

import com.kalvin.ktools.comm.kit.Shakedown12306Kit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 作用：<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年01月23日 16:35
 */
public class MainTest2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainTest2.class);

    public static void main(String[] args) {
        String trainDate = "2019-02-03";
        String fromStation = "IZQ";
        String toStation = "CBQ";
        String trainNum = "D2985,D2959,D4707,D4285,D2951,G2901,D2809,D2367,D1801,D1867";
        trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
        trainNum = "G6305,D7501,G6325,G6321,D7537,D7513,D7533,D2381,G6337,G6313,G1607,G6309,D7517,D7505";
        String seats = "M,O,N";
        try {
            Shakedown12306Kit
                    .newInstance()
                    .initUser("18218798420", "qr_kh_6926641746")
                    .initQueryInfo(trainDate, fromStation, toStation, trainNum, seats)
                    .initPassenger("刘畅澈", "445281199502020033")
                    .run();
        } catch (Exception e) {
            LOGGER.info("抢票程序已停止");
        }
    }
}
