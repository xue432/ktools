package com.kalvin.ktools;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.kit.Shakedown12306Kit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

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
//        login();
        shakdown();
    }

    public static void login() {
        HttpRequest post = HttpUtil.createPost("https://kyfw.12306.cn/passport/web/login");
        post.header("Accept", "application/json, text/javascript, */*; q=0.01");
        post.header("Accept-Encoding", "gzip, deflate, br");
        post.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        post.header("Connection", "keep-alive");
        post.header("Content-Length", "83");
        post.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.header("Host", "kyfw.12306.cn");
        post.header("Origin", "https://kyfw.12306.cn");
        post.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
        post.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        HashMap<String, Object> formData = new HashMap<>();
        formData.put("username", "");
        formData.put("password", "");
        formData.put("appid", "otn");
        formData.put("answer", "190,122,264,115");
        post.form(formData);
        String body = post.execute().body();
        LOGGER.info("login body={}", body);
    }


    public static void shakdown() {
        String trainDate = "2019-08-31";
        String fromStation = "IZQ";
        String toStation = "FAQ";
        String trainNum = "D2985,D2959,D4707,D4285,D2951,G2901,D2809,D2367,D1801,D1867";
//        trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
        trainNum = "D2834";
        String seats = "M,O,N";
        try {
            Shakedown12306Kit
                    .newInstance()
                    .initUser("", "")
                    .initQueryInfo(trainDate, fromStation, toStation, trainNum, seats)
                    .initCaptchaImgPath("C:\\Users\\Kalvin\\Desktop/captcha/")
                    .initReceiver("1481397688@qq.com")
                    .run();
        } catch (Exception e) {
            LOGGER.info("抢票程序已停止:", e.getMessage());
        }
    }

}
