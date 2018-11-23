package com.kalvin.ktools.comm.kit;

import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.BaseTest;
import org.junit.Test;

public class ImageKitTest extends BaseTest {

    @Test
    public void toBase64() {
//        File file = new File("H:\\Kalvin\\我的图片\\0.png");
//        File file = new File("H:\\Kalvin\\我的图片\\6a04b428gy1fw7qemgx9gg203w04s0zq.gif");
//        String s = ImageKit.toBase64(file);
//        LOGGER.info("s = {}", s);
    }

    @Test
    public void ipTest() {
        String ip = "119.29.193.127";
//        ip = "219.136.134.157";
        String get = HttpUtil.get("http://whois.pconline.com.cn/ip.jsp?ip=" + ip);
//        String get1 = HttpUtil.get("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
        LOGGER.info("whois_ip={}", get);
//        LOGGER.info("taobao_ip1={}", get1);
        String address = KToolkit.getIPInfo(ip);
//        String address = KToolkit.getStr();

        LOGGER.info("toolKit={}", address);
        LOGGER.info("os.name={}", System.getProperty("os.name"));


    }

    public static void main(String[] args) {
        KToolkit.imageAddWaterMark("H:\\Kalvin\\我的图片\\think.jpg",// 20090910050659531
                "http://tools.kalvinbg.cn");// http://tools.kalvinbg.cn
    }
}