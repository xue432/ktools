package com.kalvin.ktools;


import cn.hutool.core.util.ImageUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.ktools.comm.kit.ImageKit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

    public static void main(String[] args) throws Exception {
        System.out.println("isChineseByScript(\"你好\") = " + isChineseByScript("-".charAt(0)));
        System.out.println("isChinesePunctuation(\"-\") = " + isChinesePunctuation(",".charAt(0)));
        System.out.println("isCp() = " + isCp());
//        testMap();
//        crawlUrl("http://tools.kalvinbg.cn/convenience/ip");
        mn12306Login();
//        checkCaptcha(30,54,104,50,187,118);

    }

    //使用UnicodeScript方法判断
    public static boolean isChineseByScript(char c) {
        System.out.println("c = " + c);
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        return sc == Character.UnicodeScript.HAN;
    }

    // 根据UnicodeBlock方法判断中文标点符号
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS;
    }

    public static String isCp() {
        String testStr = "你好，;；。./--’Lin测 nmb试p。‘’“”";
        testStr = testStr.replaceAll("[\\pP]", "");
        Pattern pt = Pattern.compile("[^[A-Za-z]+$]");
        Matcher matcher = pt.matcher(testStr);
        while (matcher.find()) {
            MatchResult mr = matcher.toMatchResult();
            System.out.println("matches = " + mr.group());
        }

        return testStr;
    }

    public static void testMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("1", 2);
        hashMap.put("2", 3);
//        hashMap.forEach((s, i) -> System.out.println(i));
        System.out.println("hashMap = " + hashMap.get("1"));
    }

    public static void crawlUrl(String site) {

        // 模拟登录


        String url = "https://ziyuan.baidu.com/crawltools/add?site=/crawltools/add?site=" + site;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "pc");
        hashMap.put("url", site);
        hashMap.put("sid", 303287317);
        hashMap.put("zz_auto_vcode", "");
//        String post = HttpUtil.post(url, hashMap);

        HttpRequest request = HttpUtil.createPost(url);
        request.cookie("BAIDUID=159BC5C63E1373E13720D7D3FEC412C7:FG=1; BIDUPSID=159BC5C63E1373E13720D7D3FEC412C7; PSTM=1516257807; MCITY=-257%3A; lastIdentity=PassUserIdentity; Hm_lvt_6f6d5bc386878a651cb8c9e1b4a3379a=1545041256,1545042461,1545642419,1546395902; delPer=0; PSINO=7; ZD_ENTRY=empty; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; BDRCVFR[C0p6oIjvx-c]=I67x6TjHwwYf0; H_PS_PSSID=1463_21119_28206_28132_26350_28139_27542; SITEMAPSESSID=bclggvjkqs30jeekuptbos2ht4; Hm_lpvt_6f6d5bc386878a651cb8c9e1b4a3379a=1546424391; BDUSS=V2MU8zWEI4NzByYk54T3o2LTF4STBMNVVteU9Ub3l3ZjRQU0xHQ0MwTmdIVlJjQVFBQUFBJCQAAAAAAAAAAAEAAACNvL4XS2FsdmluczgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGCQLFxgkCxca2");
        request.body("type=pc&url=" + site + "&sid=303287317" + "&zz_auto_vcode=");
        HttpResponse execute = request.execute();
        String body = execute.body();
        System.out.println("body = " + body);
    }

    public static void mn12306Login() throws InterruptedException {
        Long _l = 1546591552617L;
        // 生成验证码
        String captcha = "https://kyfw.12306.cn/passport/captcha/captcha-image64";
        // 校验验证码
//        String checkCaptcha = "https://kyfw.12306.cn/passport/captcha/captcha-check?callback=jQuery19108263327514321501_1546591552466&rand=sjrand&login_site=E&_=" + (_l + 1);
        // 登录接口
        String login = "https://kyfw.12306.cn/passport/web/login";

        String params = "login_site=E&module=login&rand=sjrand&1546593264150&callback=jQuery191042896203430328805_1546497225758&_=" + _l;
        captcha += "?" + params;
        HttpRequest get = HttpUtil.createGet(captcha);
//        get.
        get.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        get.header("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        get.header("Accept-Encoding", "gzip, deflate, br");
        get.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        get.header("Host", "kyfw.12306.cn");
        get.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
        get.header("Connection", "keep-alive");
        HttpResponse execute = get.execute();

        //        get.body(params.getBytes());
        String body = execute.body();
        System.out.println("body1 = " + body);
        body = body.substring(body.indexOf("(") + 1, body.indexOf(")"));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        String image = (String) jsonObject.get("image");
        image = "data:image/png;base64," + image;
//        BufferedImage bufferedImage = ImageUtil.toImage(image);
//        ImageUtil.write(bufferedImage,  new File("C:\\Users\\14813\\Desktop\\check.png"));
        System.out.println(image);

        // checkCaptcha
//        System
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        System.out.println("answer = " + answer);

        String checkCaptcha = "https://kyfw.12306.cn/passport/captcha/captcha-check";
        String params2 = "callback=jQuery19108263327514321501_1546591552466&rand=sjrand&login_site=E&_=" + (_l + 1) + "&answer=" + answer;
        checkCaptcha += "?" + params2;
        get.setUrl(checkCaptcha);

//        Thread.sleep(5000);
        body = get.execute().body();
        System.out.println("body2 = " + body);
        body = body.substring(body.indexOf("(") + 1, body.indexOf(")"));
        jsonObject = JSONUtil.parseObj(body);
        String resultCode = jsonObject.get("result_code").toString();
        System.out.println("resultCode = " + resultCode);

        if (resultCode.equals("4")) {  // 验证码校验成功

            System.out.println("验证码校验成功");
            // login
            HttpRequest post = HttpUtil.createPost(login);
            post.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            post.header("Accept", "application/json, text/javascript, */*; q=0.01");
            post.header("Accept-Encoding", "gzip, deflate, br");
            post.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            post.header("Host", "kyfw.12306.cn");
            post.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
            post.header("Connection", "keep-alive");
            post.header("Origin", "https://kyfw.12306.cn");
            post.contentType("application/x-www-form-urlencoded; charset=UTF-8");

            HashMap<String, Object> formData = new HashMap<>();
            formData.put("username", "18218798420");
            formData.put("password", "qr_kh_6926641746");
            formData.put("appid", "otn");
            formData.put("answer", answer);
            post.form(formData);

            body = post.execute().body();

            System.out.println("login body = " + body);
        }




    }

    public static void checkCaptcha(int... letPos) throws UnsupportedEncodingException {
        Long _l = 1546591552509L;
        // 校验验证码
        String checkCaptcha = "https://kyfw.12306.cn/passport/captcha/captcha-check";
        String s = Arrays.toString(letPos);
        s = s.substring(1, s.length() - 1).replaceAll(" ", "");
        String params = "callback=jQuery19108263327514321501_1546591552466&rand=sjrand&login_site=E&_=" + (_l + 1) + "&answer=108,116";
        checkCaptcha += "?" + params;
        System.out.println("s = " + s);
//        checkCaptcha = URLEncoder.encode(checkCaptcha, "utf-8");
        System.out.println("checkCaptcha = " + checkCaptcha);
        HttpRequest get = HttpUtil.createGet(checkCaptcha);
        get.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        get.header("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        get.header("Accept-Encoding", "gzip, deflate, br");
        get.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        get.header("Host", "kyfw.12306.cn");
        get.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
        get.header("Connection", "keep-alive");

//        get.body(params.getBytes());
        String body = get.execute().body();
        System.out.println("body = " + body);

        body = body.substring(body.indexOf("(") + 1, body.indexOf(")"));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        Integer resultCode = (Integer) jsonObject.get("result_code");
        System.out.println("resultCode = " + resultCode);
        if (resultCode == 4) {  // 验证码校验成功
            System.out.println("验证码校验成功");
        }

    }
}
