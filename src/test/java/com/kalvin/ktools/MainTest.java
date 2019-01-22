package com.kalvin.ktools;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.httpclient.HttpHost;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {


    public static void main(String[] args) throws Exception {
//        System.out.println("isChineseByScript(\"你好\") = " + isChineseByScript("-".charAt(0)));
//        System.out.println("isChinesePunctuation(\"-\") = " + isChinesePunctuation(",".charAt(0)));
//        System.out.println("isCp() = " + isCp());
//        testMap();
//        crawlUrl("http://tools.kalvinbg.cn/convenience/ip");
//        mn12306Login();
//        checkCaptcha(30,54,104,50,187,118);
//        splitI();
//        handleStationNameData();
        // random参数是当前秒数*1000+毫秒数
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
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
        Long _l = 1546591552717L;// 1546931989021
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

    public static void splitI() {
        String s = "CYJQn3gydrzhsGwsIY3QDkydUxEWlw5q25Iq6FoA60pwMpVNKMMlkRb5PfCVJaez%2Bhk7lYcsabLA%0AtuPd2A3YNJ4zp6zQEOBFkhu5X03ptRWNdtsnzf2%2F6VxlXZBEaxLYGoYyh6Sbxr9%2BYjuQUtogd08p%0AghXoMgZFBm6Ky9Ce83ho1cOZOp99ASIy%2BgNy7oz2qsRtrfzRaOxuEMSaJt3mrEontnHOph7V9cV%2F%0AmtFmyKul36U39nU9g%2FAVzc37TPMN2bgTcbqCeA%2BsCOarH49TY4WJjIjM14tqIe1T8Egcpxo%3D|预订|6c000D28040A|D2804|IZQ|KQW|IZQ|FAQ|07:06|08:10|01:04|Y|nI%2F0aTvY11ZMaY4cqgc2CkZSB2porCju498cVE8f3Kwd1ZfY|20190113|3|QY|01|03|0|0|||||||有||||1|无|||O0M0O0|OMO|0|0";
        String[] split = s.split("\\|");
        System.out.println("split.len = " + split.length);
        for (int i = 0; i < split.length; i++) {
            System.out.println("sp=" + split[i] + "<-->index=" + i);
            if (StrUtil.isEmpty(split[i])) {
                System.out.println("i = " + i);
                System.out.println("split[i-1]= " + split[i-1]);
            }
        }
    }

    public void handleStationNameData() {
        List<String> list = FileUtil.readLines(new File("H:\\Kalvin\\IdeaProjects\\ktools\\src\\main\\resources\\static\\station_name_data.txt"), "utf-8");
        String stationInfo = list.get(0);
        System.out.println("stationInfo = " + stationInfo);

        String[] stationInfoArr = stationInfo.split("\\|");
        StringBuilder sb = new StringBuilder("[");
        int length = stationInfoArr.length;

        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                break;
            }
            if (i % 5 == 0) {
                String spell12306 = stationInfoArr[i + 2];
                String letter = spell12306.substring(0, 1);
                String name = stationInfoArr[i + 1];
                String allSpell = stationInfoArr[i + 3];
                if (sb.length() > 1) {
                    sb.append(",");
                }
                sb.append("{").append("letter:").append("\"").append(letter).append("\"").append(",");
                sb.append("spell12306:").append("\"").append(spell12306).append("\"").append(",");
                sb.append("name:").append("\"").append(name).append("\"").append(",");
                sb.append("allSpell:").append("\"").append(allSpell).append("\"").append("}");
            }
        }
        sb.append("]");

        System.out.println("sb = " + sb);
        JSONArray jsonArray = JSONUtil.parseArray(sb.toString());
        System.out.println("jsonArray = " + jsonArray.size());
//        Object o = jsonArray.get(0);

    }

    public void sendWithProxy() {
        /*// 设置代理IP、端口、协议（请分别替换）
        HttpHost proxy = new HttpHost("你的代理的IP", 8080);

        //把代理设置到请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        //实例化CloseableHttpClient对象
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

        //访问目标地址
        HttpGet httpGet = new HttpGet("http://www.baidu.com");

        //请求返回
        CloseableHttpResponse httpResp = httpclient.execute(httpGet);
        try {
            int statusCode = httpResp.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("成功");
            }
        } catch (Exception e) {

        } finally {
            httpResp.close();
        }*/
    }

}
