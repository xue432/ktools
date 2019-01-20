package com.kalvin.ktools;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
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

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
 * 10、getPassengerDTOs
 * 11、getPassCodeNew
 * 12、检查订单
 * 13、队列计数（准备进入下单步骤）
 * 14、正式下单
 * 15、下单确认中（调用两次）
 * 16、结果回执
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
    // getPassengerDTOs
    private static String getPassengerDTOs = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
    private static String getPassCodeNew = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew";
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

    private static String accept = "text/javascript, application/javascript, application/ecmascript,application/json, application/x-ecmascript,image/webp,image/apng,image/*,*/*; q=0.01";
    private static String l1SeatCode = "M";
    private static String l2SeatCode = "O";
    private static String noSeatCode = "N";

    private String answer;
    private long rand_;
    private String tk;
    private String cookie = "";

    private String secretStr;   // 车次加密串
    private String trainDate;   // 列车出发日期
    private String formStationName; //  始发站
    private String toStationName;   // 到达站
//    private String trainSeat;   // 最终提交的列车座席 M、O、N
    private String seatType;    // 座席类型：M、O(二等座、无座)

    private String repeatSubmitToken; // 请求提交令牌

    private String trainNo;
    private String stationTrainCode;
    private String fromStationTelecode;
    private String toStationTelecode;
    private String leftTicket;
    private String purposeCodes = "00";
    private String trainLocation;

    private String passengerTicketStr = "{seatType},0,1,{username},1,{passengerIdCard},,N";
    private String oldPassengerStr = "{username},1,{passengerIdCard},1_";

    private String keyCheckIsChange;

    private int passengerIdType = 1; // 证件类型:身份证
    private int ticketType = 1;     // 票种：成人票

    private String username;    // 乘客姓名
    private String passengerIdCard;    // 乘客身份证号

    private final List<String> dictCode = Lists.newArrayList("36,46", "116,46", "188,46", "267,43", "40,118", "113,119", "190,122", "264,115");

    public Shakedown12306Test() {
        HttpRequest get = HttpUtil.createGet("https://kyfw.12306.cn/otn/login/init");
        get.header("Host", "kyfw.12306.cn");
        get.header("Connection", "keep-alive");

        HttpResponse execute = get.execute();
        this.setCookie(execute);
        int status = execute.getStatus();
        LOGGER.info("进入12306登录页，状态码：{}", status);

//        String ua = get.header("User-Agent");
//        get.setUrl("https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=0MJE0VjA3d&hashCode=Mg-sjiOpuel38VxPEIVQ1lEonugib5Ve8Z4aZ4xk8Lk&FMQw=0&q4f3=zh-CN&VySQ=FGH_5WheGZp0a87otadDmB7aiLsm9dA9&VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=0&EOQP=8f58b1186770646318a429cb33977d8c&lEnu=167851055&jp76=52d67b2a5aa5e031084733d5006cc664&hAqN=Win32&platform=WEB&ks0Q=d22ca0b81584fbea62237b14bd04c866&TeRS=1040x1920&tOHY=24xx1080x1920&Fvje=i1l1o1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87&0aew=" + ua);
        /*String body = get.execute().body();

        body = body.substring(18, body.length() - 2);
        LOGGER.info("logdevice body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        String exp = jsonObject.get("exp").toString();  // RAIL_EXPIRATION
        String dfp = jsonObject.get("dfp").toString();  // RAIL_DEVICEID

        String rand = "1546" + RandomUtil.randomNumbers(9);
        this.rand_ = Long.valueOf(rand);*/

        this.request12306 = get;

//        this.setCookie("RAIL_EXPIRATION=" + exp + ";RAIL_DEVICEID=" + dfp);
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

    public void getCaptchaBase64() {
        String params = "?login_site=E&module=login&rand=sjrand&1546593264150&callback=jQuery191042896203430328805_1546497225758&_=" + this.rand_;
        params = "?login_site=E&module=login&rand=sjrand&" + this.genDoubleRand();

        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
//        this.request12306.header("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        this.request12306.header("Accept", accept);
        this.request12306.setUrl(captcha + params);
        String body = this.request12306.execute().body();
        LOGGER.info("body={}", body);

        body = body.substring(body.indexOf("(") + 1, body.indexOf(")"));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        String base64Image = "data:image/png;base64," + jsonObject.get("image");
        LOGGER.info(base64Image);

    }

    public void getCaptchaImage() {
        String params = "?login_site=E&module=login&rand=sjrand&" + this.genDoubleRand();
        HttpUtil.downloadFile(captcha + params, "C:\\Users\\14813\\Desktop\\check.png");
    }

    public boolean checkCaptcha() {
        Scanner scanner = new Scanner(System.in);
        String myCodes = scanner.nextLine();
        this.answer = this.getCaptchaCode(myCodes);
        LOGGER.info("answer={}", this.answer);

        this.request12306.setUrl(checkCaptcha);
        this.request12306.setMethod(Method.POST);
//        this.request12306.header("Accept", "application/json, text/javascript, */*; q=0.01");
        this.request12306.header("Accept", accept);
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
        LOGGER.info("userLogin status={}", status);
    }

    public void passport() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin");
        this.request12306.setMethod(Method.GET);
        HttpResponse execute = this.request12306.execute();
        this.setCookie(execute);
    }

    public void postUamtk(String uamtk1) {
        this.request12306.setUrl(uamtk);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("uamtk", uamtk1);
        this.request12306.form("appid", "otn");
        String body = this.request12306.execute().body();

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
            this.queryPassengerInfo();
        }
    }

    public void queryPassengerInfo() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/modifyUser/initQueryUserInfoApi");
        this.request12306.setMethod(Method.POST);
        this.setTK();
        String body = this.request12306.execute().body();
        JSON parse = JSONUtil.parse(body);
        JSONObject object = (JSONObject) parse.getByPath("data.userDTO.loginUserDTO");
//        LOGGER.info("queryInfo={}", body);
        String name = object.get("name").toString();
        Integer idTypeCode = Integer.valueOf(object.get("id_type_code").toString());
        String idNo = object.get("id_no").toString();
        this.username = name;
        this.passengerIdCard = idNo;
        LOGGER.info("name={},idTypeCode={},idNo={}", name, idTypeCode, idNo);
        this.passengerTicketStr = this.passengerTicketStr.replace("{username}", name).replace("{passengerIdCard}", idNo);
        this.oldPassengerStr = this.oldPassengerStr.replace("{username}", name).replace("{passengerIdCard}", idNo);
        LOGGER.info("passengerTicketStr={},oldPassengerStr={}", this.passengerTicketStr, this.oldPassengerStr);
    }

    public void initTicket() {
        String params = "?linktypeid=dc&fs=广州,GZQ,GZQ&ts=怀集,FAQ,FAQ&date=2019-01-13&flag=N,N,Y";
        this.request12306.setUrl(initTicket + params);
        String cookie = "_jc_save_fromStation=%u5E7F%u5DDE%2CGZQ; _jc_save_toStation=%u6000%u96C6%2CFAQ; _jc_save_fromDate=2019-02-08; _jc_save_toDate=2019-01-15; _jc_save_wfdc_flag=dc; current_captcha_type=Z";
        this.setCookie(cookie);

        this.setTK();

    }

    public boolean queryZ(String trainDates, String formStation, String toStation, String trainNums, String seats, boolean isLogin) {

        initTicket();

        for (String trainDate : trainDates.split(",")) {
            String params = "?leftTicketDTO.train_date="+trainDate+"&leftTicketDTO.from_station="+formStation+"&leftTicketDTO.to_station="+toStation+"&purpose_codes=ADULT";
            return this.queryZAndSubmitOrder(params, trainDate, trainNums, seats, isLogin);
        }

        return false;

    }

    /**
     * 查票并提交订单或有票车次进行入库处理
     * @param params 车票查询接口参数
     * @param trainDate 出发日期
     * @param trainNums 车次
     * @param seats 座席：M、O、N
     * @param isLogin 是否登录状态
     * @return
     */
    public boolean queryZAndSubmitOrder(String params, String trainDate, String trainNums, String seats, boolean isLogin) {
        HttpRequest request12306;
        request12306 = HttpUtil.createGet(queryZ + params);
        request12306.setUrl(queryZ + params);
        request12306.setMethod(Method.GET);
        request12306.removeHeader("Origin");
        request12306.removeHeader("uamtk");
        request12306.header("Accept", "*/*");
//        request12306.header("Referer", "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc&fs=%E5%B9%BF%E5%B7%9E,GZQ&ts=%E6%80%80%E9%9B%86,FAQ&date=2019-01-15&flag=N,N,Y");
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
            boolean submitFlg = false;
            String trainNum = ts.getTrainNum();
            String l1Seat = ts.getL1Seat();
            String l2Seat = ts.getL2Seat();
            String noSeat = ts.getNoSeat(); // todo 无座标识

            LOGGER.info("可预订车票：发车日期：{}，车次：{}，座席：一等座{}、二等座{}、无座{}", trainDate, trainNum, l1Seat, l2Seat, noSeat);
            if (trainNums.contains(trainNum)) { // 当前车次有票

                // 先进行一次解码。避免提交后再编码一次导致参数失效
                this.secretStr = this.urlDecode(ts.getSecretStr());
                this.leftTicket = this.urlDecode(ts.getLeftTicket());

                this.trainNo = ts.getTrainNo();
                this.trainDate = trainDate;
                this.formStationName = ts.getFormStationName();
                this.toStationName = ts.getToStationName();
                this.stationTrainCode = trainNum;
                this.fromStationTelecode = ts.getFormStationTelecode();
                this.toStationTelecode = ts.getToStationTelecode();
                this.trainLocation = ts.getTrainLocation();

                // 优先考虑二等座
                if (seats.contains(l2SeatCode)) {
                    submitFlg = (NumberUtil.isNumber(l2Seat) && Integer.valueOf(l2Seat) > 0) || "有".equals(l2Seat);
                    this.seatType = l2SeatCode;
                    LOGGER.info("提交订单：车次：{}，二等座，发车日期：{}，座席：{}", trainNum, trainDate, l2SeatCode);
                } else if (seats.contains(l1SeatCode)) {    // 其次一等座
                    submitFlg = (NumberUtil.isNumber(l1Seat) && Integer.valueOf(l1Seat) > 0) || "有".equals(l1Seat);
                    this.seatType = l1SeatCode;
                    LOGGER.info("提交订单：车次：{}，一等座，发车日期：{}，座席：{}", trainNum, trainDate, l1SeatCode);
                } else if (seats.contains(noSeatCode)) {    // 最后选择无座
                    submitFlg = (NumberUtil.isNumber(noSeat) && Integer.valueOf(noSeat) > 0) || "有".equals(noSeat);
                    this.seatType = l2SeatCode; // 无座 座席类型和二等座一样是：O
                    LOGGER.info("提交订单：车次：{}，无座，发车日期：{}，座席：{}", trainNum, trainDate, noSeatCode);
                }

                LOGGER.info("submitFlg={}", submitFlg);
                if (submitFlg) {    // 提交订单
                    if (isLogin) {
                        this.passengerTicketStr = this.passengerTicketStr.replace("{seatType}", this.seatType);
//                        this.submitOrderRequest();
//                        this.initDc();
                        return true;
                    } else {
                        // todo 入队列
                        LOGGER.info("车次：{}入队列", trainNum);
                    }
                }
            }
        }
        return false;
    }

    public void checkUser() {
        this.request12306.setUrl(checkUser);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer",  "https://kyfw.12306.cn/otn/leftTicket/init");
        this.request12306.execute();
    }

    public void submitOrderRequest() {

        // checkUser
        this.checkUser();

        LOGGER.info("secretStr={},trainDate={},formStationName={},toStationName={}",
                secretStr, trainDate, formStationName, toStationName);

        this.request12306.setUrl(submitOrderRequest);
        this.request12306.setMethod(Method.POST);

        String cookie1 = request12306.header("Cookie");
        LOGGER.info("submitOrderRequest cookie={}", cookie1);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("secretStr", this.secretStr);
        hashMap.put("train_date", this.trainDate);
        hashMap.put("back_train_date", DateUtil.format(new Date(), "yyyy-MM-dd")); // 返程日
        hashMap.put("tour_flag", "dc"); // 单程
        hashMap.put("purpose_codes", "ADULT");  // 成人票
        hashMap.put("query_from_station_name", "广州");   // todo
        hashMap.put("query_to_station_name", this.toStationName);
        hashMap.put("undefined", "");
        this.request12306.form(hashMap);

        String body = this.request12306.execute().body();
        LOGGER.info("submitOrderRequest={}", body);
        JSONObject jsonObject = JSONUtil.parseObj(body);
        boolean status = (boolean) jsonObject.get("status");
        if (status) {
            String data = jsonObject.get("data").toString();
            if ("N".equals(data)) {
                // todo checkOrderInfo
                this.initDc();
                this.checkOrderInfo();
            }
        } else {
            // todo
            this.submitOrderRequest();
        }

    }

    public void initDc() {
        this.request12306.setUrl(initDc);
        this.request12306.setMethod(Method.POST);
        String cookie = this.request12306.header("Cookie");
        LOGGER.info("initDc cookie={}", cookie);
        this.setTK();

        String body = this.request12306.execute().body();
//        LOGGER.info("initDc={}", body);
        List<String> list0 = ReUtil.findAll("globalRepeatSubmitToken = '(.*?)'", body, 1);
        List<String> list1 = ReUtil.findAll("'key_check_isChange':'(.*?)'", body, 1);
        this.repeatSubmitToken = list0.get(0);
        this.keyCheckIsChange = list1.get(0);

        this.getJS();
        this.getPassengerDTOs();
        this.getPassCodeNew();
    }

    public void getJS() {
        this.request12306.setUrl(getJS);
        this.request12306.setMethod(Method.GET);
        this.request12306.execute();
    }

    public void getPassengerDTOs() {
        this.request12306.setUrl(getPassengerDTOs);
        this.request12306.setMethod(Method.POST);
        this.request12306.form("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);
        this.request12306.form("_json_att", "");
        String body = this.request12306.execute().body();
        LOGGER.info("getPassengerDTOs body = {}", body);
    }

    public void getPassCodeNew() {
        String params = "?module=passenger&rand=randp&" + this.genDoubleRand();
        this.request12306.setUrl(getPassCodeNew + params);
        this.request12306.setMethod(Method.GET);
        HttpResponse execute = this.request12306.execute();
        LOGGER.info("getPassCodeNew status = {}", execute.getStatus());
        this.setCookie(execute);
    }

    public void checkOrderInfo() {
        this.request12306.setUrl(checkOrderInfo);
        this.request12306.setMethod(Method.POST);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cancel_flag", 2);
        hashMap.put("bed_level_order_num", "000000000000000000000000000000");
        hashMap.put("passengerTicketStr", this.passengerTicketStr);
        hashMap.put("oldPassengerStr", this.oldPassengerStr);
        hashMap.put("tour_flag", "dc"); // 单程
        hashMap.put("randCode", "");
        hashMap.put("whatsSelect", this.passengerIdType);   // 证件类型:1身份证
        hashMap.put("_json_att", "");
        hashMap.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        this.request12306.form(hashMap);

        String body = this.request12306.execute().body();
        JSON parse = JSONUtil.parse(body);
        boolean submitStatus = (boolean) parse.getByPath("data.submitStatus");
        String ifShowPassCode = (String) parse.getByPath("data.ifShowPassCode");
        String ifShowPassCodeTime = (String) parse.getByPath("data.ifShowPassCodeTime");
        LOGGER.info("checkOrderInfo body = {}", body);
        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"ifShowPassCode":"N","canChooseBeds":"N","canChooseSeats":"Y","choose_Seats":"O","isCanChooseMid":"N","ifShowPassCodeTime":"1189","submitStatus":true,"smokeStr":""},"messages":[],"validateMessages":{}}
        boolean isNeedCode = false;
        if (submitStatus) {
            LOGGER.info("车票提交通过，正在尝试排队");
            if ("Y".equals(ifShowPassCode)) {
                isNeedCode = true;
            }
        }

    }

    public void getQueueCount() {
        this.request12306.setUrl(getQueueCount);
        this.request12306.setMethod(Method.POST);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("train_date", this.formatDateGMT(this.trainDate));
        hashMap.put("train_no", this.trainNo);
        hashMap.put("stationTrainCode", this.stationTrainCode);
        hashMap.put("seatType", this.seatType);
        hashMap.put("fromStationTelecode", this.fromStationTelecode);
        hashMap.put("toStationTelecode", this.toStationTelecode);
        hashMap.put("leftTicket", this.leftTicket);
        hashMap.put("purpose_codes", this.purposeCodes);
        hashMap.put("train_location", this.trainLocation);
        hashMap.put("_json_att", "");
        hashMap.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        this.request12306.form(hashMap);

        HttpResponse execute = this.request12306.execute();
        LOGGER.info("getQueueCount body = {}", execute.body());
    }

    public void confirmSingleForQueue() {
        this.request12306.setUrl(confirmSingleForQueue);
        this.request12306.setMethod(Method.POST);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("passengerTicketStr", this.passengerTicketStr);
        hashMap.put("oldPassengerStr", this.oldPassengerStr);
        hashMap.put("randCode", "");
        hashMap.put("purpose_codes", this.purposeCodes);
        hashMap.put("key_check_isChange", this.keyCheckIsChange);
        hashMap.put("leftTicketStr", this.leftTicket);
        hashMap.put("train_location", this.trainLocation);
        hashMap.put("choose_seats", "");
        hashMap.put("seatDetailType", "000");   // 开始需要选择座位，但是目前12306不支持自动选择作为，那这个参数为默认
        hashMap.put("whatsSelect", "1");
        hashMap.put("roomType", "00");  // 好像是根据一个id来判断选中的，两种 第一种是00，第二种是10，但是我在12306的页面没找到该id，目前写死是00
        hashMap.put("dwAll", "N");
        hashMap.put("_json_att", "");
        hashMap.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        this.request12306.form(hashMap);

        HttpResponse execute = this.request12306.execute();
        LOGGER.info("confirmSingleForQueue body = {}", execute.body());
    }

    /**
     * 排队获取订单等待信息,每隔3秒请求一次，最高请求次数为20次！
     */
    public void queryOrderWaitTime() {
        String params = "?random=" + this.genRand13() + "&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=" + this.repeatSubmitToken;
        this.request12306.setUrl(queryOrderWaitTime + params);
        this.request12306.setMethod(Method.GET);

        HttpResponse execute = this.request12306.execute();
        LOGGER.info("queryOrderWaitTime body = {}", execute.body());
    }

    public void resultOrderForDcQueue() {
        this.request12306.setUrl(resultOrderForDcQueue);
        this.request12306.setMethod(Method.POST);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderSequence_no", "EF95296121");  // todo
        hashMap.put("_json_att", "");
        hashMap.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        this.request12306.form(hashMap);

        HttpResponse execute = this.request12306.execute();
        LOGGER.info("resultOrderForDcQueue body = {}", execute.body());
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

    public double genDoubleRand() {
        return RandomUtil.randomDouble(0, 0.9, 17, RoundingMode.HALF_UP);
    }

    public String genRand13() {
        return  "1546" + RandomUtil.randomNumbers(9);
    }

    public String urlDecode(String str) {
        try {
            // 先进行一次解码。避免提交后再编码一次导致参数secretStr失效
            String dStr = URLDecoder.decode(str, "utf-8");
            LOGGER.info("dStr={}", dStr);
            return dStr;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
        return str;
    }

    public String formatDateGMT(String date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy '00:00:00' 'GMT'Z '(中国标准时间)'", Locale.ENGLISH);
        return sdf.format(date);
    }

    public List<TrainSchedule> handleTrainInfo(String body) {
        JSON parse = JSONUtil.parse(body);
        JSONObject map = (JSONObject) parse.getByPath("data.map");
        JSONArray result = (JSONArray) parse.getByPath("data.result");
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
        if (s12306.checkCaptcha()) {
            s12306.login12306("18218798420", "qr_kh_6926641746");
            boolean isSubmit = s12306.queryZ("2019-02-08", "GZQ", "FAQ",
                    "D2804,D1842,D2962,D2842,D1872", "M,O,N", true);
            if (isSubmit) {
                s12306.submitOrderRequest();
            }
        } else {
            // todo 重新获取验证码并登录
            LOGGER.info("重新获取验证码并登录");
        }

//        s12306.queryZ("2019-01-13", "GZQ", "FAQ",
//                "D2804,D1842,D2962", "M,O,N", false);

    }


}
