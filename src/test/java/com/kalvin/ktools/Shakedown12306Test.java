package com.kalvin.ktools;


import com.kalvin.ktools.comm.kit.Shakedown12306Kit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抢票流程：
 * 1、生成验证码
 * 2、校验验证码
 * 3、密码登录
 * 4、获取token
 * 5、查票
 * 6、checkUser
 * 7、提交订单
 * 8、initDc
 * 9、GetJS
 * 10、getPassengerDTOs
 * 11、getPassCodeNew
 * 12、检查订单
 * 13、队列计数（准备进入下单步骤）
 * 14、正式下单
 * 15、下单确认中（至少调用两次）
 * 16、结果回执
 */
public class Shakedown12306Test {

    private final static Logger LOGGER = LoggerFactory.getLogger(Shakedown12306Test.class);

    public static void main(String[] args) {

        String trainDate = "2019-02-03";
        String fromStation = "IZQ";
        String toStation = "FAQ";
        String trainNum = "D2985,D2959,D4707,D4285,D2951,G2901,D2809,D2367,D1801,D1867";
//        trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
        trainNum = "D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876,D2842";
        String seats = "M,O,N";
        try {
            Shakedown12306Kit
                    .newInstance()
                    .initUser("15816541383", "XIExie569447830")
                    .initQueryInfo(trainDate, fromStation, toStation, trainNum, seats)
                    .initCaptchaImgPath("C:/Users/14813/Desktop/captcha/")
                    .initReceiver("1481397688@qq.com")
                    .run();
        } catch (Exception e) {
            LOGGER.info("抢票程序已停止:", e.getMessage());
        }
    }


}
