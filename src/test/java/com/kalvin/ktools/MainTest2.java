package com.kalvin.ktools;

import cn.hutool.extra.mail.MailUtil;

import java.util.Arrays;

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
        String[] ticketSplit = {"12", "2", "9"};
        long count = Arrays.stream(ticketSplit).map(Integer::valueOf).reduce(0, Integer::sum);
        System.out.println("count = " + count);
    }
}
