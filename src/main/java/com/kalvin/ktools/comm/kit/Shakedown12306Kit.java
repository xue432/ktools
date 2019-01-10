package com.kalvin.ktools.comm.kit;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

/**
 * 12306抢票工具
 */
public class Shakedown12306Kit {

    public static HttpRequest initReqGet(String url) {
        HttpRequest get = HttpUtil.createGet(url);
        get.header("Host", "kyfw.12306.cn");
        get.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
        get.header("Connection", "keep-alive");
//        HttpRequest request12306 = get;
        return get;
    }
}
