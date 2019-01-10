package com.kalvin.ktools;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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
 * 10、检查订单
 * 11、队列计数（准备进入下单步骤）
 * 12、正式下单
 * 13、下单确认中（调用两次）
 * 14、结果回执
 */
public class Shakedown12306Test {

    private final static Logger LOGGER = LoggerFactory.getLogger(Shakedown12306Test.class);

    private HttpRequest request12306;
    // 生成验证码
    private static String captcha = "https://kyfw.12306.cn/passport/captcha/captcha-image";
    // 校验验证码
    private static String checkCaptcha = "https://kyfw.12306.cn/passport/captcha/captcha-check";
    // 密码登录
    private static String login = "https://kyfw.12306.cn/passport/web/login";
    // 获取token
    private static String uamtk = "https://kyfw.12306.cn/passport/web/auth/uamtk";
    private static String uamauthclient = "https://kyfw.12306.cn/otn/uamauthclient";
    // 查票
    private static String initTicket = "https://kyfw.12306.cn/otn/leftTicket/init";
    private static String queryZ = "https://kyfw.12306.cn/otn/leftTicket/queryZ";
    // checkUser
    private static String checkUser = "https://kyfw.12306.cn/otn/login/checkUser";
    // 提交订单
    private static String submitOrderRequest = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
    // initDc
    private static String initDc = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
    // GetJS
    private static String getJS = "https://kyfw.12306.cn/otn/HttpZF/GetJS";
    // 检查订单
    private static String checkOrderInfo = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
    // 队列计数（准备进入下单步骤）
    private static String getQueueCount = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
    // 正式下单
    private static String confirmSingleForQueue = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
    // 下单确认中（调用两次）
    private static String queryOrderWaitTime = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=1546856953746&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=5edfe6853ea5f997ad29f54a512727c0";
    // 结果回执
    private static String resultOrderForDcQueue = "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue";

    private static String l1SeatCode = "M";
    private static String l2SeatCode = "O";
    private static String noSeatCode = "N";

    private String answer;
    private long rand_;
    private String tk;
    private String cookie = "";
    private String username;

    private final List<String> dictCode = Lists.newArrayList("36,46", "116,46", "188,46", "267,43", "40,118", "113,119", "190,122", "264,115");

    public Shakedown12306Test() {
        HttpRequest get = HttpUtil.createGet("https://kyfw.12306.cn/otn/login/init");
        get.header("Host", "kyfw.12306.cn");
        get.header("Connection", "keep-alive");

        HttpResponse execute = get.execute();
        this.setCookie(execute);
        int status = execute.getStatus();
        LOGGER.info("进入12306登录页，状态码：{}", status);

        String ua = get.header("User-Agent");
        get.setUrl("https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=0MJE0VjA3d&hashCode=Mg-sjiOpuel38VxPEIVQ1lEonugib5Ve8Z4aZ4xk8Lk&FMQw=0&q4f3=zh-CN&VySQ=FGH_5WheGZp0a87otadDmB7aiLsm9dA9&VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=0&EOQP=8f58b1186770646318a429cb33977d8c&lEnu=167851055&jp76=52d67b2a5aa5e031084733d5006cc664&hAqN=Win32&platform=WEB&ks0Q=d22ca0b81584fbea62237b14bd04c866&TeRS=1040x1920&tOHY=24xx1080x1920&Fvje=i1l1o1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87&0aew=" + ua);
        String body = get.execute().body();

        body = body.substring(18, body.length() - 2);
        LOGGER.info("logdevice body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        String exp = jsonObject.get("exp").toString();  // RAIL_EXPIRATION
        String dfp = jsonObject.get("dfp").toString();  // RAIL_DEVICEID

        String rand = "1546" + RandomUtil.randomNumbers(9);
        this.rand_ = Long.valueOf(rand);

        this.request12306 = get;

        this.setCookie("RAIL_EXPIRATION=" + exp + ";RAIL_DEVICEID=" + dfp);
        this.setTK();
    }

    public HttpRequest getHttpRequest() {
        return this.request12306;
    }

    private void setTK() {
        this.request12306.header("Cookie", this.cookie);
    }

    private void setCookie(HttpResponse response) {
        List<HttpCookie> cookies = response.getCookies();
        cookies.forEach(hc -> {
            if (StrUtil.isNotEmpty(this.cookie)) {
                this.cookie += ";";
            }
            this.cookie += hc.toString();
        });
    }

    private void setCookie(String cookie) {
        if (StrUtil.isNotEmpty(this.cookie)) {
            this.cookie += ";";
        }
        this.cookie += cookie;
    }

    /**
     * 获取验证码位置代码
     * @param myCodes 1~8，多个使用英文逗号分隔
     * @return
     */
    public String getCaptchaCode(String myCodes) {
        StringBuilder captchaCode = new StringBuilder();
        String[] myCodeArr = myCodes.split("");
        if (myCodeArr.length == 0) {
            return captchaCode.toString();
        }
        for (String myCode : myCodeArr) {
            if (captchaCode.length() > 0) {
                captchaCode.append(",");
            }
            captchaCode.append(this.dictCode.get(Integer.valueOf(myCode) - 1));
        }
        return captchaCode.toString();
    }

    public void getCaptchaBase64() {
        double dr = RandomUtil.randomDouble(0, 0.9, 17, RoundingMode.HALF_UP);
        LOGGER.info("dr = {}", dr);
        String params = "?login_site=E&module=login&rand=sjrand&1546593264150&callback=jQuery191042896203430328805_1546497225758&_=" + this.rand_;
        // 0.36080305656170086
        params = "?login_site=E&module=login&rand=sjrand&" + dr;

        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
//        this.request12306.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        this.request12306.header("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
//        this.request12306.header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        this.request12306.setUrl(captcha + params);
        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);

        body = body.substring(body.indexOf("(") + 1, body.indexOf(")"));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        String base64Image = "data:image/png;base64," + jsonObject.get("image");
        LOGGER.info(base64Image);

    }

    public void getCaptchaImage() {
        double dr = RandomUtil.randomDouble(0, 0.9, 17, RoundingMode.HALF_UP);
        LOGGER.info("dr = {}", dr);
        String params = "?login_site=E&module=login&rand=sjrand&" + dr;
        HttpUtil.downloadFile(captcha + params, "C:\\Users\\Kalvin\\Desktop\\check.png");
//        this.request12306.setUrl(captc)
    }

    public boolean isSuccessCatcha2() {
        Scanner scanner = new Scanner(System.in);
        String myCodes = scanner.nextLine();
        LOGGER.info("myCodes={}", myCodes);

        this.answer = this.getCaptchaCode(myCodes);
        LOGGER.info("answer={}", this.answer);

        this.request12306.setUrl(checkCaptcha);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Accept", "application/json, text/javascript, */*; q=0.01");
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/login/init");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("answer", answer);
        hashMap.put("login_site", "E");
        hashMap.put("rand", "sjrand");
        this.request12306.form(hashMap);
        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        String resultCode = jsonObject.get("result_code").toString();
        LOGGER.info("resultCode={}", resultCode);

        if ("4".equals(resultCode)) {
            LOGGER.info("验证码校验成功");
            return true;
        }
        return false;
    }

    public boolean isSuccessCaptcha() {

        // todo
        Scanner scanner = new Scanner(System.in);
        String myCodes = scanner.nextLine();
        LOGGER.info("myCodes={}", myCodes);

        this.answer = this.getCaptchaCode(myCodes);
        LOGGER.info("answer={}", this.answer);
        String params = "?callback=jQuery19108263327514321501_1546591552466&rand=sjrand&login_site=E&_=" + (this.rand_ + 1) + "&answer=" + this.answer;

        this.request12306.setUrl(checkCaptcha);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("answer", answer);
        hashMap.put("login_site", "E");
        hashMap.put("rand", "sjrand");
        this.request12306.form(hashMap);
        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);
        body = body.substring(body.indexOf("(") + 1, body.indexOf(")"));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        String resultCode = jsonObject.get("result_code").toString();
        LOGGER.info("resultCode={}", resultCode);

        if ("4".equals(resultCode)) {
            LOGGER.info("验证码校验成功");
            return true;
        }
        return false;
    }

    public void login12306(String username, String password) {
        this.request12306.setUrl(login);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Origin", "https://kyfw.12306.cn");

        HashMap<String, Object> formData = new HashMap<>();
        formData.put("username", username);    // 18218798420
        formData.put("password", password);   // qr_kh_6926641746
        formData.put("appid", "otn");
        formData.put("answer", this.answer);
        this.request12306.form(formData);

        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        Integer resultCode = (Integer) jsonObject.get("result_code");
        if (resultCode == 0) {   // 登录成功，获取tk
            this.userLogin();
            this.passport();
            this.postUamtk((String) jsonObject.get("uamtk"));
        } else {
            // todo 重新获取验证码并登录
            LOGGER.info("重新获取验证码并登录");
        }
    }

    public void userLogin() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/login/userLogin");
        this.request12306.setMethod(Method.GET);
        HttpResponse execute = this.request12306.execute();
        int status = execute.getStatus();
        LOGGER.info("status={}", status);
    }

    public void passport() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin");
        this.request12306.setMethod(Method.GET);
        HttpResponse execute = this.request12306.execute();
        this.setCookie(execute);
        String body = execute.body();
//        LOGGER.info("passport body={}", body);
    }

    public void postUamtk(String uamtk1) {
        LOGGER.info("uamtk={}", uamtk);
        this.request12306.setUrl(uamtk);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("uamtk", uamtk1);
        this.request12306.form("appid", "otn");
        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        Integer resultCode = (Integer) jsonObject.get("result_code");
        if (resultCode == 0) {
            String newapptk = (String) jsonObject.get("newapptk");
            LOGGER.info("newapptk={}", newapptk);
            this.tk = newapptk;
            this.postUamauthclient();
        }
    }

    public void postUamauthclient() {
        this.request12306.setUrl(uamauthclient);
        this.request12306.setMethod(Method.POST);
        this.request12306.form("tk", this.tk);
        HttpResponse execute = this.request12306.execute();
        this.setCookie(execute);

        String cookieStr = execute.getCookieStr();
        LOGGER.info("tk={},cookieStr={}", tk, cookieStr);
        String body = execute.body();
        LOGGER.info("body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        Integer resultCode = (Integer) jsonObject.get("result_code");
        if (resultCode == 0) {
            this.username = (String) jsonObject.get("username");
//            this.queryInfo();
        }
    }

    public void queryInfo() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/modifyUser/initQueryUserInfoApi");
        this.request12306.setMethod(Method.POST);
        this.setTK();
        String body = this.request12306.execute().body();
        LOGGER.info("queryInfo={}", body);
    }

    public void initTicket() {
        String params = "?linktypeid=dc&fs=广州,GZQ,GZQ&ts=怀集,FAQ,FAQ&date=2019-01-13&flag=N,N,Y";
        this.request12306.setUrl(initTicket + params);
        String cookie = "_jc_save_fromStation=%u5E7F%u5DDE%2CGZQ; _jc_save_toStation=%u6000%u96C6%2CFAQ; _jc_save_fromDate=2019-01-13; _jc_save_toDate=2019-01-10; _jc_save_wfdc_flag=dc; current_captcha_type=Z";
//        this.request12306.header("Cookie", this.cookie + cookie);
        this.setCookie(cookie);

        this.setTK();

    }

    public void queryZ(String trainDates, String formStation, String toStation, String trainNums, String seats, boolean isLogin) {

        initTicket();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String trainDate : trainDates.split(",")) {
            String params = "?leftTicketDTO.train_date="+trainDate+"&leftTicketDTO.from_station="+formStation+"&leftTicketDTO.to_station="+toStation+"&purpose_codes=ADULT";
            boolean b = this.queryZAndSubmitOrder(params, trainDate, trainNums, seats, isLogin);
            if (b) break;
        }

    }

    /**
     * 查票并提交订单或有票车次进行入库处理
     *
     * @param trainDate 列车出发日期
     * @param formStation 列车出发站
     * @param toStation 列车终点站
     */
    public boolean queryZAndSubmitOrder(String params, String trainDate, String trainNums, String seats, boolean isLogin) {
//        String params = "?leftTicketDTO.train_date="+trainDate+"&leftTicketDTO.from_station="+formStation+"&leftTicketDTO.to_station="+toStation+"&purpose_codes=ADULT";

//        Shakedown12306Test s12306 = new Shakedown12306Test();
//        HttpRequest request = s12306.getHttpRequest();


        HttpRequest request12306;
        request12306 = HttpUtil.createGet(queryZ + params);
//        request12306 = this.request12306;
        request12306.setUrl(queryZ + params);
        request12306.setMethod(Method.GET);
        request12306.removeHeader("Origin");
        request12306.removeHeader("uamtk");
        request12306.header("Accept", "*/*");
        request12306.header("Referer", "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc");
        request12306.header("Cookie", this.cookie);
        request12306.header("Host", "kyfw.12306.cn");
        request12306.header("Connection", "keep-alive");

        Map<String, List<String>> headers = request12306.headers();
        headers.forEach((k, l) -> LOGGER.info("{}:{}", k, l.get(0)));

        HttpResponse execute = request12306.execute();
        String body = execute.body();
        LOGGER.info("body={}", body);

        this.request12306 = request12306;

        List<TrainSchedule> scheduleList = this.handleTrainInfo(body);
        for (TrainSchedule ts : scheduleList) {
            String trainNum = ts.getTrainNum();
            if (trainNums.contains(trainNum)) { // 当前车次有票
                String l1Seat = ts.getL1Seat();
                String l2Seat = ts.getL2Seat();
                String noSeat = ts.getNoSeat();

                // 优先考虑二等座
                if (seats.contains(l2Seat)) {
                    if (NumberUtil.isNumber(l2Seat) && Integer.valueOf(l2Seat) > 0) {
                        if (isLogin) {
                            // todo 提交订单
                            LOGGER.info("提交订单：车次：{}，二等座，发车日期：{}，座席：{}", trainNum, trainDate, l2SeatCode);
                            this.submitOrderRequest(ts.getSecretStr(), trainDate, ts.getFormStationName(), ts.getToStationName());
//                            this.initDc();
                            return true;
                        } else {
                            // todo 有票车次进行入库处理
                        }

                    } else if ("有".equals(l2Seat)) {
                        if (isLogin) {
                            // todo 提交订单
                            LOGGER.info("提交订单：车次：{}，二等座，发车日期：{}，座席：{}", trainNum, trainDate, l2SeatCode);
                            this.submitOrderRequest(ts.getSecretStr(), trainDate, ts.getFormStationName(), ts.getToStationName());
//                            this.initDc();
                            return true;
                        }

                    }
                }

                // 其次一等座
                if (seats.contains(l1SeatCode)) {
                    if (NumberUtil.isNumber(l1Seat) && Integer.valueOf(l1Seat) > 0) {
                        LOGGER.info("提交订单：车次：{}，一等座，发车日期：{}，座席：{}", trainNum, trainDate, l1SeatCode);
                    } else if ("有".equals(l1Seat)) {
                        LOGGER.info("提交订单：车次：{}，一等座，发车日期：{}，座席：{}", trainNum, trainDate, l1SeatCode);
                    }
                }

                // 最后选择无座
                if (seats.contains(noSeatCode)) {
                    if (NumberUtil.isNumber(noSeat) && Integer.valueOf(noSeat) > 0) {
                        if (isLogin) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            LOGGER.info("提交订单：车次：{}，无座，发车日期：{}，座席：{}", trainNum, trainDate, noSeatCode);
                            this.submitOrderRequest(ts.getSecretStr(), trainDate, ts.getFormStationName(), ts.getToStationName());
//                            this.initDc();
                            return true;
                        } else {

                        }

                    } else if ("有".equals(noSeat)) {
                        if (isLogin) {
                            LOGGER.info("提交订单：车次：{}，无座，发车日期：{}，座席：{}", trainNum, trainDate, noSeatCode);
                            this.submitOrderRequest(ts.getSecretStr(), trainDate, ts.getFormStationName(), ts.getToStationName());
//                            this.initDc();
                            return true;
                        } else {

                        }
                    }
                }

            } else {
                LOGGER.info("当前车次{}有票，但不符合当前用户抢票条件", trainNum);
            }
        }
        return false;

//        scheduleList.forEach(System.out::println);
    }

    /**
     * 查票 带tk
     * 查到票马上提交订单
     * @param trainDate 列车出发日期
     * @param formStation 列车出发站
     * @param toStation 列车终点站
     */
    public void queryZWithTK(String trainDate, String formStation, String toStation) {
        String params = "?leftTicketDTO.train_date="+trainDate+"&leftTicketDTO.from_station="+formStation+"&leftTicketDTO.to_station="+toStation+"&purpose_codes=ADULT";
        this.request12306.setUrl(queryZ + params);
        this.request12306.setMethod(Method.GET);
//        this.setTK();

        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);
    }

    public void checkUser() {
        this.request12306.setUrl(checkUser);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer",  "https://kyfw.12306.cn/otn/leftTicket/init");
//        this.setTK();
        String cookie = this.request12306.header("Cookie");
        LOGGER.info("checkUser cookie={}", cookie);

        String body = this.request12306.execute().body();
        LOGGER.info("checkUser body={}", body);
    }

    public void submitOrderRequest(String secretStr, String trainDate, String formStationName, String toStationName) {
        // checkUser
        this.checkUser();

        LOGGER.info("secretStr={},trainDate={},formStationName={},toStationName={}",
                secretStr, trainDate, formStationName, toStationName);

        HttpRequest request12306;
        request12306 = HttpUtil.createPost(submitOrderRequest);
        request12306 = this.request12306;
        request12306.setUrl(submitOrderRequest);
        request12306.setMethod(Method.POST);
//        this.setTK();
        String cookie = ";route=9036359bb8a8a461c164a04f8f50b252; RAIL_EXPIRATION=1547374564430; RAIL_DEVICEID=rGmhlViizyzMN4K9aVfwuSlB_el8ItIuUoWCqhLgtPdI82RK5wCTjAwnRu1Mf6ee0hsnGW0GxhnwhNIThiFHhu5kt5CIZozWl6_qVaTMsTNebEOXjdq6sFofAxhgT1aOkLqjcq6ecE2ZMOTr_jrBqrcweFJPHGCG; BIGipServerpassport=937951498.50215.0000; BIGipServerpool_passport=200081930.50215.0000; _jc_save_fromStation=%u5E7F%u5DDE%2CGZQ; _jc_save_toStation=%u6000%u96C6%2CFAQ; _jc_save_fromDate=2019-01-13; _jc_save_toDate=2019-01-10; _jc_save_wfdc_flag=dc; current_captcha_type=Z; BIGipServerotn=484966666.24610.0000";
//        request12306.header("Cookie", this.cookie + cookie);
//        request12306.header("Referer",  "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc&fs=%E5%B9%BF%E5%B7%9E,GZQ&ts=%E6%80%80%E9%9B%86,FAQ&date=2019-01-13&flag=N,N,Y");
//        request12306.header("Host", "kyfw.12306.cn");
//        request12306.header("Connection", "keep-alive");
//        request12306.header("Origin","https://kyfw.12306.cn");
//        request12306.header("Accept","*/*");

        String cookie1 = request12306.header("Cookie");
        LOGGER.info("submitOrderRequest cookie={}", cookie1);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("secretStr", secretStr);
        hashMap.put("train_date", trainDate);
        hashMap.put("back_train_date", DateUtil.format(new Date(), "yyyy-MM-dd")); // 返程日
        hashMap.put("tour_flag", "dc");
        hashMap.put("purpose_codes", "ADULT");
        hashMap.put("query_from_station_name", "广州");   // todo
        hashMap.put("query_to_station_name", toStationName);
        hashMap.put("undefined", "");
        request12306.form(hashMap);

        String body = request12306.execute().body();
        LOGGER.info("submitOrderRequest={}", body);

    }

    public void initDc() {
        this.request12306.setUrl(initDc);
        this.request12306.setMethod(Method.POST);
        String cookie = this.request12306.header("Cookie");
        LOGGER.info("initDc cookie={}", cookie);
        this.setTK();

        String body = this.request12306.execute().body();
        LOGGER.info("initDc={}", body);
    }

    public List<TrainSchedule> handleTrainInfo(String body) {
        JSON parse = JSONUtil.parse(body);
        JSONObject map = (JSONObject) parse.getByPath("data.map");
        JSONArray result = (JSONArray) parse.getByPath("data.result");
//        LOGGER.info("result={}", result);
        List<TrainSchedule> scheduleList = new ArrayList<>();
        TrainSchedule trainSchedule;

        for (Object object : result) {
            trainSchedule = this.new TrainSchedule();
            String trainInfo = object.toString();
            String[] split = trainInfo.split("\\|");

            if (StrUtil.isNotEmpty(split[0])) {
                trainSchedule.setSecretStr(split[0]);
                trainSchedule.setTrainNo(split[2]);
                trainSchedule.setTrainNum(split[3]);
                trainSchedule.setFormStationTelecode(split[6]);
                trainSchedule.setToStationTelecode(split[7]);
                trainSchedule.setGoOffTime(split[8]);
                trainSchedule.setArrivalTime(split[9]);
                trainSchedule.setLastTime(split[10]);
                trainSchedule.setLeftTicket(split[12]);
                trainSchedule.setTrainLocation(split[15]);
//            trainSchedule.setBusinessSeat(split[32]);   // or 5 todo
                trainSchedule.setL1Seat(split[31]);
                trainSchedule.setL2Seat(split[30]);
                trainSchedule.setL1SoftBerth(split[23]);
                trainSchedule.setL2HardBerth(split[28]);
                trainSchedule.setHardSeat(split[29]);
                trainSchedule.setNoSeat(split[26]);

                trainSchedule.setFormStationName(map.get(split[6]).toString());
                trainSchedule.setToStationName(map.get(split[7]).toString());

                scheduleList.add(trainSchedule);
            }
        }

        scheduleList.forEach(System.out::println);
        return scheduleList;
    }

    /**
     * 列车信息实体
     */
    public class TrainSchedule {

        private String secretStr;   // 密钥串
        private String trainNo;     // 列车号
        private String trainNum;    // 车次
        private String formStationName; // 始发站名称
        private String toStationName; // 终点站名称
        private String formStationTelecode; // 12306始发站代码
        private String toStationTelecode;   // 12306终点站代码
        private String goOffTime;   // 出发时间
        private String arrivalTime; // 到达时间
        private String lastTime;    // 历时时间
        private String leftTicket;
        private String trainLocation;

        private String businessSeat;    // 商务座
        private String l1Seat;  // 一等座
        private String l2Seat;  // 二等座
        private String l1SoftBerth; // 软卧一等卧
        private String l2HardBerth; // 硬卧二等卧
        private String hardSeat;    // 硬座
        private String noSeat;      // 无座

        public String getSecretStr() {
            return secretStr;
        }

        public void setSecretStr(String secretStr) {
            this.secretStr = secretStr;
        }

        public String getTrainNo() {
            return trainNo;
        }

        public void setTrainNo(String trainNo) {
            this.trainNo = trainNo;
        }

        public String getTrainNum() {
            return trainNum;
        }

        public void setTrainNum(String trainNum) {
            this.trainNum = trainNum;
        }

        public String getFormStationName() {
            return formStationName;
        }

        public void setFormStationName(String formStationName) {
            this.formStationName = formStationName;
        }

        public String getToStationName() {
            return toStationName;
        }

        public void setToStationName(String toStationName) {
            this.toStationName = toStationName;
        }

        public String getFormStationTelecode() {
            return formStationTelecode;
        }

        public void setFormStationTelecode(String formStationTelecode) {
            this.formStationTelecode = formStationTelecode;
        }

        public String getToStationTelecode() {
            return toStationTelecode;
        }

        public void setToStationTelecode(String toStationTelecode) {
            this.toStationTelecode = toStationTelecode;
        }

        public String getGoOffTime() {
            return goOffTime;
        }

        public void setGoOffTime(String goOffTime) {
            this.goOffTime = goOffTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getLeftTicket() {
            return leftTicket;
        }

        public void setLeftTicket(String leftTicket) {
            this.leftTicket = leftTicket;
        }

        public String getTrainLocation() {
            return trainLocation;
        }

        public void setTrainLocation(String trainLocation) {
            this.trainLocation = trainLocation;
        }

        public String getBusinessSeat() {
            return businessSeat;
        }

        public void setBusinessSeat(String businessSeat) {
            this.businessSeat = businessSeat;
        }

        public String getL1Seat() {
            return l1Seat;
        }

        public void setL1Seat(String l1Seat) {
            this.l1Seat = l1Seat;
        }

        public String getL2Seat() {
            return l2Seat;
        }

        public void setL2Seat(String l2Seat) {
            this.l2Seat = l2Seat;
        }

        public String getL1SoftBerth() {
            return l1SoftBerth;
        }

        public void setL1SoftBerth(String l1SoftBerth) {
            this.l1SoftBerth = l1SoftBerth;
        }

        public String getL2HardBerth() {
            return l2HardBerth;
        }

        public void setL2HardBerth(String l2HardBerth) {
            this.l2HardBerth = l2HardBerth;
        }

        public String getHardSeat() {
            return hardSeat;
        }

        public void setHardSeat(String hardSeat) {
            this.hardSeat = hardSeat;
        }

        public String getNoSeat() {
            return noSeat;
        }

        public void setNoSeat(String noSeat) {
            this.noSeat = noSeat;
        }

        @Override
        public String toString() {
            return "TrainSchedule{" +
                    "secretStr='" + secretStr + '\'' +
                    ", trainNo='" + trainNo + '\'' +
                    ", trainNum='" + trainNum + '\'' +
                    ", formStationName='" + formStationName + '\'' +
                    ", toStationName='" + toStationName + '\'' +
                    ", formStationTelecode='" + formStationTelecode + '\'' +
                    ", toStationTelecode='" + toStationTelecode + '\'' +
                    ", goOffTime='" + goOffTime + '\'' +
                    ", arrivalTime='" + arrivalTime + '\'' +
                    ", lastTime='" + lastTime + '\'' +
                    ", leftTicket='" + leftTicket + '\'' +
                    ", trainLocation='" + trainLocation + '\'' +
                    ", businessSeat='" + businessSeat + '\'' +
                    ", l1Seat='" + l1Seat + '\'' +
                    ", l2Seat='" + l2Seat + '\'' +
                    ", l1SoftBerth='" + l1SoftBerth + '\'' +
                    ", l2HardBerth='" + l2HardBerth + '\'' +
                    ", hardSeat='" + hardSeat + '\'' +
                    ", noSeat='" + noSeat + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        Shakedown12306Test s12306 = new Shakedown12306Test();
        s12306.getCaptchaImage();
        if (s12306.isSuccessCatcha2()) {
            s12306.login12306("18218798420", "qr_kh_6926641746");
            s12306.queryZ("2019-01-13", "GZQ", "FAQ",
                    "D2804,D1842,D2962,D2842", "M,O,N", true);
        } else {
            // todo 重新获取验证码并登录
            LOGGER.info("重新获取验证码并登录");
        }

//        s12306.queryZ("2019-01-13", "GZQ", "FAQ",
//                "D2804,D1842,D2962", "M,O,N", false);

//        HttpRequest request12306 = HttpUtil.createPost(Shakedown12306Test.submitOrderRequest);
//        request12306.header("Cookie", "JSESSIONID=2951DC09EAD8CEFCC978403A2DB97CD7; tk=ct9YmKzLkoVMb3wLOrNaILcYq1oOtdCL0oLSCjbQSfo921210; route=9036359bb8a8a461c164a04f8f50b252; RAIL_EXPIRATION=1547374564430; RAIL_DEVICEID=rGmhlViizyzMN4K9aVfwuSlB_el8ItIuUoWCqhLgtPdI82RK5wCTjAwnRu1Mf6ee0hsnGW0GxhnwhNIThiFHhu5kt5CIZozWl6_qVaTMsTNebEOXjdq6sFofAxhgT1aOkLqjcq6ecE2ZMOTr_jrBqrcweFJPHGCG; BIGipServerpassport=937951498.50215.0000; BIGipServerpool_passport=200081930.50215.0000; _jc_save_fromStation=%u5E7F%u5DDE%2CGZQ; _jc_save_toStation=%u6000%u96C6%2CFAQ; _jc_save_toDate=2019-01-10; _jc_save_wfdc_flag=dc; current_captcha_type=Z; BIGipServerotn=770703882.38945.0000; _jc_save_fromDate=2019-01-13");
//        request12306.header("Referer",  "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc&fs=%E5%B9%BF%E5%B7%9E,GZQ&ts=%E6%80%80%E9%9B%86,FAQ&date=2019-01-13&flag=N,N,Y");
//        request12306.header("Host", "kyfw.12306.cn");
//        request12306.header("Connection", "keep-alive");
//        request12306.header("Origin","https://kyfw.12306.cn");
//        request12306.header("Accept","*/*");
//
//        String format = DateUtil.format(new Date(), "yyyy-MM-dd");
//        LOGGER.info("format={}", format);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("secretStr", "jDiKahaKKm0oAGMNa4MdYRvD8VH3%2F8NEr0wmJlSG7TJlPA7CLPBaPW3TU%2FZBfDk83sWGfYeCGQmZ%0A4yPcOVbKOBnQO%2Bi%2Fi7znRr3TxQDO2rC9qs%2FSoiCijCsnOBaZDBJwPaq1xLR0VX2Tge0Ob23Zar6%2B%0AB6Oe%2BCpibvgpULZAZOiqxunipINWEE4U2%2Bo92UvD96RcqaCN%2F6lLM1%2F%2FgriyDAwKCWn3zdND0dLf%0A%2F3%2Bmou1mIhDLOdRtoOkpXeR49gAjsocriJPascNshc9uc7JlJXXQXxh5eRjYUH0rni20VGg%3D");
//        hashMap.put("train_date", "2019-01-13");
//        hashMap.put("back_train_date", format); // 返程日
//        hashMap.put("tour_flag", "dc");
//        hashMap.put("purpose_codes", "ADULT");
//        hashMap.put("query_from_station_name", "广州");
//        hashMap.put("query_to_station_name", "怀集");
//        hashMap.put("undefined", "");
//        request12306.form(hashMap);
//
//        String body = request12306.execute().body();
//        LOGGER.info("submitOrderRequest={}", body);

    }


}
