package com.kalvin.ktools;

import cn.hutool.extra.mail.MailUtil;
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
        String src = "C:/Users/Kalvin/Desktop/water.png";
//        src = "C:/Users/Kalvin/Desktop/one.jpg";
        src = "C:/Users/Kalvin/Desktop/check.png";
//        KToolkit.imageAddWaterMark(src, "http://tools.kalvinbg.cn");// http://tools.kalvinbg.cn // 20090910050659531

//        MailUtil.sendText("1481397688@qq.com", "测试发送邮件", "测试发送邮件123");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println("线程启动：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "结束");
            });
        }
    }
}
