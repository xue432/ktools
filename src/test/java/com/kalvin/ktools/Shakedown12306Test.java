package com.kalvin.ktools;


import cn.hutool.core.date.DateTime;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private HttpRequest request12306;

    private static String loginInit = "https://kyfw.12306.cn/otn/login/init";
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
    private static String queryOrderWaitTime = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";
    // 结果回执
    private static String resultOrderForDcQueue = "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue";

    private static String accept = "text/javascript, application/javascript, application/ecmascript,application/json, application/x-ecmascript,image/webp,image/apng,image/*,*/*; q=0.01";
    private static String l1SeatCode = "M";
    private static String l2SeatCode = "O";
    private static String noSeatCode = "N";

    private String username;    // 12306用户账号
    private String password;    // 密码

    /*乘客订票相关参数*/
    private String trainDates;
    private String fromStation;
    private String toStation;
    private String trainNums;
    private String seats;

    private String answer;
    private String tk;
    private String cookie = "";

    private String queryFromStationName;    // 查询始发站
    private String queryToStationName;  // 查询到达站

    private String secretStr;   // 车次加密串
    private String trainDate;   // 列车出发日期
    private String formStationName; //  始发站
    private String toStationName;   // 到达站
    private String seatType;    // 座席类型：M、O(二等座、无座)

    private String repeatSubmitToken; // 请求提交令牌

    private String trainNo;
    private String stationTrainCode;
    private String fromStationTelecode;
    private String toStationTelecode;
    private String leftTicket;
    private String purposeCodes;
    private String trainLocation;

    private String passengerTicketStr = "{seatType},0,1,{username},1,{passengerIdCard},,N";
    private String oldPassengerStr = "{username},1,{passengerIdCard},1_";

    private String keyCheckIsChange;

    private int passengerIdType = 1; // 证件类型:身份证
    private int ticketType = 1;     // 票种：成人票

    private String passenger;    // 乘客姓名
    private String passengerIdCard;    // 乘客身份证号

    private boolean isNeedCode; // 提交订单时是否需要验证码
    private int ifShowPassCodeTime; // 显示验证码时间，单位：毫秒

    private String leftTicketStr;

    private int outNum = 120;   // 排队请求12306的次数

    private int ticketCount;    // 余票数

    private boolean useProxy;

    private final List<String> dictCode = Lists.newArrayList("36,46", "116,46", "188,46", "267,43", "40,118", "113,119", "190,122", "264,115");

    private int btn;    // 刷票次数

    public Shakedown12306Test(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 进入登录页
     */
    private void loginInit() {
        this.request12306 = HttpUtil.createGet(loginInit);
        this.request12306.header("Host", "kyfw.12306.cn");
        this.request12306.header("Connection", "keep-alive");

        if (useProxy) {
            this.openProxy();
        }

        HttpResponse execute = this.request12306.execute();
        this.setCookie(execute);
        int status = execute.getStatus();
        LOGGER.info("进入12306登录页，状态码：{}", status);

        /*String ua = get.header("User-Agent");
        get.setUrl("https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=0MJE0VjA3d&hashCode=Mg-sjiOpuel38VxPEIVQ1lEonugib5Ve8Z4aZ4xk8Lk&FMQw=0&q4f3=zh-CN&VySQ=FGH_5WheGZp0a87otadDmB7aiLsm9dA9&VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=0&EOQP=8f58b1186770646318a429cb33977d8c&lEnu=167851055&jp76=52d67b2a5aa5e031084733d5006cc664&hAqN=Win32&platform=WEB&ks0Q=d22ca0b81584fbea62237b14bd04c866&TeRS=1040x1920&tOHY=24xx1080x1920&Fvje=i1l1o1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87&0aew=" + ua);
        String body = get.execute().body();

        body = body.substring(18, body.length() - 2);
        LOGGER.info("logdevice body={}", body);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        String exp = jsonObject.get("exp").toString();  // RAIL_EXPIRATION
        String dfp = jsonObject.get("dfp").toString();  // RAIL_DEVICEID
        this.setCookie("RAIL_EXPIRATION=" + exp + ";RAIL_DEVICEID=" + dfp);*/

    }

    /**
     * 下载图片验证码
     * @param type 图片验证码类型
     */
    private void getCaptchaImage(CaptchaImageType type) {
        String params;
        this.request12306.setMethod(Method.GET);

        if (type.value == 0) {
            params = "?login_site=E&module=login&rand=sjrand&" + this.genDoubleRand();
            this.request12306.setUrl(captcha + params);
            HttpResponse httpResponse = this.request12306.executeAsync();
            httpResponse.writeBody(new File("C:\\Users\\Kalvin\\Desktop\\check.png"));
        }
        if (type.value == 1) {
            params = "?module=passenger&rand=randp&" + this.genDoubleRand();
            this.request12306.setUrl(captcha + params);
            HttpResponse httpResponse = this.request12306.executeAsync();
            httpResponse.writeBody(new File("C:\\Users\\Kalvin\\Desktop\\orderCheck.png"));
        }
    }

    /**
     * 验证图片验证码
     * @return bl
     */
    private boolean authCaptchaImage() {
        Scanner scanner = new Scanner(System.in);
        String myCodes = scanner.nextLine();
        this.answer = this.getCaptchaCode(myCodes);
        LOGGER.info("answer={}", this.answer);

        this.request12306.setUrl(checkCaptcha);
        this.request12306.setMethod(Method.POST);
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

    /**
     * 登录12306
     */
    private void login() {
        this.request12306.setUrl(login);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Origin", "https://kyfw.12306.cn");

        HashMap<String, Object> formData = new HashMap<>();
        formData.put("username", this.username);
        formData.put("password", this.password);
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

    private void userLogin() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/login/userLogin");
        this.request12306.setMethod(Method.GET);
        HttpResponse execute = this.request12306.execute();
        execute.getStatus();
    }

    private void passport() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin");
        this.request12306.setMethod(Method.GET);
        HttpResponse execute = this.request12306.execute();
        this.setCookie(execute);
    }

    /**
     * 获取登录token
     * @param uamtk1 uam
     */
    private void postUamtk(String uamtk1) {
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

    /**
     * 获取权限
     */
    private void postUamauthclient() {
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
            this.queryPassengerInfo();
        }
    }

    /**
     * 获取用户信息
     */
    private void queryPassengerInfo() {
        this.request12306.setUrl("https://kyfw.12306.cn/otn/modifyUser/initQueryUserInfoApi");
        this.request12306.setMethod(Method.POST);

        String body = this.request12306.execute().body();
        JSON parse = JSONUtil.parse(body);
        JSONObject object = (JSONObject) parse.getByPath("data.userDTO.loginUserDTO");
//        LOGGER.info("queryInfo={}", body);
        String name = object.get("name").toString();
        Integer idTypeCode = Integer.valueOf(object.get("id_type_code").toString());
        String idNo = object.get("id_no").toString();
        this.passenger = name;
        this.passengerIdCard = idNo;
        LOGGER.info("name={},idTypeCode={},idNo={}", name, idTypeCode, idNo);
        this.passengerTicketStr = this.passengerTicketStr.replace("{username}", name).replace("{passengerIdCard}", idNo);
        this.oldPassengerStr = this.oldPassengerStr.replace("{username}", name).replace("{passengerIdCard}", idNo);
        LOGGER.info("passengerTicketStr={},oldPassengerStr={}", this.passengerTicketStr, this.oldPassengerStr);
    }

    /**
     * 进入查票页面
     */
    private void initTicket() {
        this.request12306.setUrl(initTicket);
        this.request12306.execute();
    }

    /**
     * 查票。查询多个日期
     * @return bl:true-有票 false-无票
     */
    public boolean queryZAll() {
//        initTicket();
        for (String trainDate : this.trainDates.split(",")) {
            String params = "?leftTicketDTO.train_date="+trainDate+"&leftTicketDTO.from_station="+this.fromStation+"&leftTicketDTO.to_station="+this.toStation+"&purpose_codes=ADULT";
            boolean b = this.queryZ(params, trainDate);
            if (b) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查票
     * @param params 车票查询接口参数
     * @param trainDate 出发日期
     * @return bl:true-有票 false-无票
     */
    private boolean queryZ(String params, String trainDate) {
        try {
            this.request12306 = HttpUtil.createGet(queryZ + params);

            // 由于这是新创建的请求，如果使用代理IP，需要再打开一次
            if (useProxy) {
                this.openProxy();
            }

            this.request12306.setUrl(queryZ + params);
            this.request12306.header("Accept", accept);
            this.request12306.header("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
            this.request12306.header("Cookie", this.cookie);
            this.request12306.header("Host", "kyfw.12306.cn");
            this.request12306.header("Connection", "keep-alive");
//            this.request12306.setFollowRedirects(false);

            HttpResponse execute = this.request12306.execute();
            String body = execute.body();
//            LOGGER.info("query status = {}，body={}", execute.getStatus(), body);

            List<TrainSchedule> scheduleList = this.handleTrainInfo(body);
            for (TrainSchedule ts : scheduleList) {
                boolean submitFlg = false;
                String trainNum = ts.getTrainNum();
                String l1Seat = ts.getL1Seat();
                String l2Seat = ts.getL2Seat();
                String noSeat = ts.getNoSeat(); // todo 无座标识

                LOGGER.info("可预订车票：发车日期：{}，车次：{}，出发时间：{}，到达时间：{}，座席：一等座{}、二等座{}、无座{}", trainDate, trainNum, ts.getGoOffTime(), ts.getArrivalTime(), l1Seat, l2Seat, noSeat);
                if (this.trainNums.contains(trainNum)) { // 当前车次有票

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

                    String seatName = "";

                    /* 优先考虑二等座,其次一等座,最后选择无座 */
                    if (this.seats.contains(l2SeatCode)) {
                        submitFlg = (NumberUtil.isNumber(l2Seat) && Integer.valueOf(l2Seat) > 0) || "有".equals(l2Seat);
                        this.seatType = l2SeatCode;
                        seatName = "二等座";
                    }
                    if (this.seats.contains(l1SeatCode) && !submitFlg) {
                        submitFlg = (NumberUtil.isNumber(l1Seat) && Integer.valueOf(l1Seat) > 0) || "有".equals(l1Seat);
                        this.seatType = l1SeatCode;
                        seatName = "一等座";
                    }
                    if (this.seats.contains(noSeatCode) && !submitFlg) {
                        submitFlg = (NumberUtil.isNumber(noSeat) && Integer.valueOf(noSeat) > 0) || "有".equals(noSeat);
                        this.seatType = l2SeatCode; // 无座 座席类型和二等座一样是：O
                        seatName = "无座";
                    }

                    if (submitFlg) {    // 提交订单
                        LOGGER.info("提交订单：车次：{}，{}，发车日期：{}，座席类型：{}", trainNum, seatName, trainDate, this.seatType);
                        if (StrUtil.isNotEmpty(this.tk)) {
                            this.passengerTicketStr = this.passengerTicketStr.replace("{seatType}", this.seatType);
                            return true;
                        } else {
                            // todo 入队列
                            LOGGER.info("车次：{}入队列", trainNum);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("查询订单异常：{}", e.getMessage());
        }

        this.btn++;
        LOGGER.info("-------已刷票{}次--------", btn);
        return false;
    }

    /**
     * 检查用户
     */
    private void checkUser() {
        this.request12306.setUrl(checkUser);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer",  "https://kyfw.12306.cn/otn/leftTicket/init");
        String body = this.request12306.execute().body();
        LOGGER.info("checkUser body={}", body);
    }

    /**
     * 提交订单
     */
    private void submitOrderRequest(int submitTime) {

        this.checkUser();

        LOGGER.info("secretStr={},trainDate={},formStationName={},toStationName={}",
                secretStr, trainDate, this.queryFromStationName, this.queryToStationName);

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
        hashMap.put("query_from_station_name", this.queryFromStationName);
        hashMap.put("query_to_station_name", this.queryToStationName);
        hashMap.put("undefined", "");
        this.request12306.form(hashMap);

        boolean isReSubmit = false;

        try {
            String body = this.request12306.execute().body();
            LOGGER.info("submitOrderRequest={}", body);
            JSONObject jsonObject = JSONUtil.parseObj(body);
            boolean status = (boolean) jsonObject.get("status");

            if (status) {
                this.initDc();
                String data = jsonObject.get("data").toString();
                if ("N".equals(data)) {
                    this.checkOrderInfo();
                }
            } else {
                // todo 是否需要再次提交当前订单
                isReSubmit = true;
                LOGGER.info("订单提交失败，信息：{}", jsonObject.get("messages"));

            }
        } catch (Exception e) {
            isReSubmit = true;
        }

        if (isReSubmit) {
            if (submitTime > 2) {
                LOGGER.info("---------------请重新登录----------------");
                this.run(false);    // 重新登录
            }
            submitTime++;
            this.submitOrderRequest(submitTime);
        }
    }

    /**
     * initDc
     */
    private void initDc() {
        this.request12306.setUrl(initDc);
        this.request12306.setMethod(Method.POST);

        String body = this.request12306.execute().body();
//        LOGGER.info("initDc={}", body);
        List<String> list0 = ReUtil.findAll("globalRepeatSubmitToken = '(.*?)'", body, 1);
        List<String> list1 = ReUtil.findAll("'key_check_isChange':'(.*?)'", body, 1);
        List<String> list2 = ReUtil.findAll("'purpose_codes':'(.*?)'", body, 1);
        List<String> list3 = ReUtil.findAll("'leftTicketStr':'(.*?)'", body, 1);
        this.repeatSubmitToken = list0.get(0);
        this.keyCheckIsChange = list1.get(0);
        this.purposeCodes = list2.get(0);
        this.leftTicketStr = list3.get(0);

        this.getJS();
        this.getPassengerDTOs();
        this.getPassCodeNew();
    }

    /**
     * 获取initDc页面中的JS脚本
     */
    private void getJS() {
        this.request12306.setUrl(getJS);
        this.request12306.setMethod(Method.GET);
        this.request12306.execute();
    }

    private void getPassengerDTOs() {
        this.request12306.setUrl(getPassengerDTOs);
        this.request12306.setMethod(Method.POST);
        this.request12306.form("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);
        this.request12306.form("_json_att", "");
        this.request12306.execute().body();
    }

    /**
     * 获取订单图片验证码
     */
    private void getPassCodeNew() {
        this.getCaptchaImage(CaptchaImageType.ORDER);
    }

    /**
     * 检查订单正确性
     */
    private void checkOrderInfo() {
        this.request12306.setUrl(checkOrderInfo);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cancel_flag", 2);
        hashMap.put("bed_level_order_num", "000000000000000000000000000000");
        hashMap.put("passengerTicketStr", this.passengerTicketStr);
        hashMap.put("oldPassengerStr", this.oldPassengerStr);
        hashMap.put("tour_flag", "dc"); // 单程
        hashMap.put("randCode", "");
        hashMap.put("whatsSelect", 1);   // 1-成人票 2-学生票
        hashMap.put("_json_att", "");
        hashMap.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        this.request12306.form(hashMap);

        String body = this.request12306.execute().body();
        JSON parse = JSONUtil.parse(body);
        LOGGER.info("checkOrderInfo body = {}", body);
        boolean submitStatus = (boolean) parse.getByPath("data.submitStatus");
        String ifShowPassCode = (String) parse.getByPath("data.ifShowPassCode");
        this.ifShowPassCodeTime = Integer.valueOf((String) parse.getByPath("data.ifShowPassCodeTime"));
        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"ifShowPassCode":"N","canChooseBeds":"N","canChooseSeats":"Y","choose_Seats":"O","isCanChooseMid":"N","ifShowPassCodeTime":"1189","submitStatus":true,"smokeStr":""},"messages":[],"validateMessages":{}}
        if (submitStatus) {
            LOGGER.info("车票提交通过，正在尝试排队");
            this.isNeedCode = "Y".equals(ifShowPassCode);
        }

        this.getQueueCount();
    }

    /**
     * 获取订单队列位置
     */
    private void getQueueCount() {
        this.request12306.setUrl(getQueueCount);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");

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
        String body = execute.body();
        LOGGER.info("getQueueCount body = {}", body);
        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"count":"3","ticket":"0,154","op_2":"false","countT":"0","op_1":"true"},"messages":[],"validateMessages":{}}
        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"count":"0","ticket":"151,72","op_2":"false","countT":"0","op_1":"false"},"messages":[],"validateMessages":{}}

        JSONObject object = JSONUtil.parseObj(body);
        JSONObject dataObj = (JSONObject) object.get("data");
        boolean status = (boolean) object.get("status");
        if (status) {
            String ticket = dataObj.get("ticket").toString();
            Integer count = Integer.valueOf(dataObj.get("count").toString());   // todo 到底是这个是排队位置还是countT 我这里使用count
            Integer countT = Integer.valueOf(dataObj.get("countT").toString());
            LOGGER.info("countT={}", countT);

            if (!ticket.contains(",")) {
                this.ticketCount = Integer.valueOf(ticket);
            } else {
                String[] ticketSplit = ticket.split(",");
                this.ticketCount = Arrays.stream(ticketSplit).map(Integer::valueOf).max(Integer::compareTo).get();
            }

            LOGGER.info("排队成功，你排在：{}位，当前余票还有：{}张", count, ticketCount);
            this.confirmSingleForQueue();
        } else {
            LOGGER.info("排队错误，错误信息：{}", object.get("messages"));
        }
    }

    /**
     * 真正下订单
     */
    private void confirmSingleForQueue() {

        String randCode = "";
        if (this.isNeedCode) {  // todo
            LOGGER.info("需要订单验证码，请输入验证码");
            Scanner scanner = new Scanner(System.in);
            String myCodes = scanner.nextLine();
            randCode = this.getCaptchaCode(myCodes);
            LOGGER.info("randCode={}", randCode);
        } else {
            LOGGER.info("不需要订单验证码，直接提交");
        }

        LOGGER.info("ifShowPassCodeTime = {}", this.ifShowPassCodeTime);
        this.sleep(this.ifShowPassCodeTime);   // 睡眠0.5秒

        this.request12306.setUrl(confirmSingleForQueue);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");

        String cookie = this.request12306.header("Cookie");
        LOGGER.info("confirmSingleForQueue cookie = {}", cookie);

        LOGGER.info("passengerTicketStr={},oldPassengerStr={},purpose_codes={},key_check_isChange={},leftTicketStr={},train_location={}",
                this.passengerTicketStr, this.oldPassengerStr, this.purposeCodes, this.keyCheckIsChange, this.leftTicket, this.trainLocation);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("passengerTicketStr", this.passengerTicketStr);
        hashMap.put("oldPassengerStr", this.oldPassengerStr);
        hashMap.put("randCode", randCode);
        hashMap.put("purpose_codes", this.purposeCodes);
        hashMap.put("key_check_isChange", this.keyCheckIsChange);
        hashMap.put("leftTicketStr", this.leftTicketStr);   // 这个参数不需要解码的。
        hashMap.put("train_location", this.trainLocation);
        hashMap.put("choose_seats", "");
        hashMap.put("seatDetailType", "000");   // 选择座位，不选默认000
        hashMap.put("whatsSelect", 1);    // 1-成人票 2-学生票
        hashMap.put("roomType", "00");  // 好像是根据一个id来判断选中的，两种 第一种是00，第二种是10，但是我在12306的页面没找到该id，目前写死是00
        hashMap.put("dwAll", "N");
        hashMap.put("_json_att", "");
        hashMap.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        this.request12306.form(hashMap);

        HttpResponse execute = this.request12306.execute();
        String body = execute.body();
        LOGGER.info("confirmSingleForQueue body = {}", body);
        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"submitStatus":true},"messages":[],"validateMessages":{}}

        boolean isReLogin = false;

        JSON parse = JSONUtil.parse(body);
        if ((boolean) parse.getByPath("status")) {
            if ((boolean) parse.getByPath("data.submitStatus")) {
                this.queryOrderWaitTime();
            } else {
                isReLogin = true;
                LOGGER.info("正式下单失败，{}", parse.getByPath("data.errMsg"));
            }
        } else {
            isReLogin = true;
            LOGGER.info("正式下单失败，{}", parse.getByPath("messages"));
        }

        if (isReLogin) {
            LOGGER.info("-------------------正式下单失败，请重新登录--------------------------");
            this.run(false);
        }
    }

    /**
     * 排队获取订单等待信息,每隔3秒请求一次，最高请求次数为20次！
     */
    private void queryOrderWaitTime() {
        this.request12306.setMethod(Method.GET);
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");

        boolean isReLogin = false;

        int tryTimes = 0;
        while (true) {
            if (tryTimes > this.outNum) {
                // todo 排队失败，取消订单
                LOGGER.info("排队失败，取消订单");
                break;
            }
            try {
                String params = "?random=" + this.currentTimeMillis() + "&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=" + this.repeatSubmitToken;
                this.request12306.setUrl(queryOrderWaitTime + params);
                HttpResponse execute = this.request12306.execute();
                String body = execute.body();
                LOGGER.info("queryOrderWaitTime body = {}", body);

                JSON parse = JSONUtil.parse(body);
                if ((boolean) parse.getByPath("status")) {
                    JSONObject dObj = (JSONObject) parse.getByPath("data");
                    String orderId = dObj.get("orderId").toString();
                    String waitCount = dObj.get("waitCount").toString();
                    LOGGER.info("余票剩余：{}张，前面还有{}位等待出票", this.ticketCount, waitCount);
                    LOGGER.info("orderId={}", orderId);
                    if (StrUtil.isNotBlank(orderId) && !"null".equals(orderId) && orderId != null) {
                        // todo 订票成功，使用邮件通知抢票人
                        LOGGER.info("恭喜您订票成功，订单号为：{}, 请立即打开浏览器登录12306，访问‘未完成订单’，在30分钟内完成支付!", orderId);
                        break;
                    } else {
                        LOGGER.info("正在排队，排队时间剩余：{}毫秒", dObj.get("waitTime"));
                    }
                } else {
                    LOGGER.info("排队失败，{}", parse.getByPath("messages"));
                }

                tryTimes++;
                LOGGER.info("第{}次排队中...", tryTimes);
            } catch (Exception e) {
                isReLogin = true;
                LOGGER.error("排队失败，{}", e.getMessage());
            }
            this.sleep(2000);
        }
        if (isReLogin) {
            LOGGER.info("-------------------排队失败，请重新登录--------------------------");
            this.run(false);
        }
        // {“validateMessagesShowId”:”_validatorMessage”,”status”:true,”httpstatus”:200,”data”:{“queryOrderWaitTimeStatus”:true,”count”:0,”waitTime”:17,”requestId”:6217964314520123645,”waitCount”:366,”tourFlag”:”dc”,”orderId”:null},”messages”:[],”validateMessages”:{}}
        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"queryOrderWaitTimeStatus":true,"count":0,"waitTime":4,"requestId":6493308224569738742,"waitCount":0,"tourFlag":"dc","orderId":null},"messages":[],"validateMessages":{}}

    }

    /**
     * 查询未完成的订单 todo
     */
    public void queryMyOrderNoComplete() {

    }

    /**
     * 取消未完成的订单 todo
     */
    public void cancelNoCompleteMyOrder() {

    }

    /**
     * 下单成功回执。暂时无用
     */
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
     * 打开代理
     */
    private void openProxy() {
        // https://www.xicidaili.com/nn/{页码} // 代理IP获取地址
        // 代理ip:119.101.115.114
        SocketAddress sa = new InetSocketAddress("112.91.218.21", 	9000);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
        this.request12306.setProxy(proxy);
        LOGGER.info("当前使用代理IP：{}", proxy.toString());
    }

    /**
     * 初始化订票参数信息
     * @param trainDates 列车出发日期
     * @param fromStation 始发站
     * @param toStation 终点站
     * @param trainNums 列表车次
     * @param seats 座席类型：M、O、N
     */
    public void initQueryInfo(String trainDates, String fromStation, String toStation, String trainNums, String seats) {
        this.trainDates = trainDates;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.trainNums = trainNums;
        this.seats = seats;
    }

    private void setTK() {
        this.request12306.header("Cookie", this.cookie);
    }

    /**
     * 设置Cookie
     * @param response 响应报文
     */
    private void setCookie(HttpResponse response) {
        List<HttpCookie> cookies = response.getCookies();
        cookies.forEach(hc -> {
            if (StrUtil.isNotEmpty(this.cookie)) {
                this.cookie += ";";
            }
            this.cookie += hc.toString();
        });
        this.request12306.header("Cookie", this.cookie);
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

    public double genDoubleRand() {
        return RandomUtil.randomDouble(0, 0.9, 17, RoundingMode.HALF_UP);
    }

    public long currentTimeMillis() {
        return  System.currentTimeMillis();
    }

    public String urlDecode(String str) {
        try {
            // 先进行一次解码。避免提交后再编码一次导致参数secretStr失效
            String dStr = URLDecoder.decode(str, "utf-8");
            LOGGER.info("urlDecode={}", dStr);
            return dStr;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
        return str;
    }

    public String formatDateGMT(String dateStr) {
        final DateTime date = DateUtil.parse(dateStr);
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy '00:00:00' 'GMT'Z '(中国标准时间)'", Locale.ENGLISH);
        return sdf.format(date);
    }

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<TrainSchedule> handleTrainInfo(String body) {
        JSON parse = JSONUtil.parse(body);
        JSONObject map = (JSONObject) parse.getByPath("data.map");
        JSONArray result = (JSONArray) parse.getByPath("data.result");
        List<TrainSchedule> scheduleList = new ArrayList<>();
        TrainSchedule trainSchedule;

        // 转化查询的始发站和到达站成中文
        this.queryFromStationName = map.get(this.fromStation).toString();
        this.queryToStationName = map.get(this.toStation).toString();

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
    class TrainSchedule {

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

    /**
     * 验证码图片类型
     */
    enum CaptchaImageType {
        LOGIN(0),ORDER(1);

        private Integer value;

        CaptchaImageType(Integer value) {
            this.value = value;
        }
    }
    /**
     * 开始抢票。不使用代理IP
     */
    public void run() {
        loginInit();
        this.getCaptchaImage(CaptchaImageType.LOGIN);
        int submitTime = 0;
        if (this.authCaptchaImage()) {
            this.login();
            this.initTicket();
            while (!this.queryZAll()) {
                this.sleep(5000);
            }
            this.submitOrderRequest(submitTime);
        } else {
            // todo 重新获取验证码并登录
            LOGGER.info("重新获取验证码并登录");
        }
    }

    /**
     * 开始抢票。使用代理IP
     * @param useProxy 是否开启代理IP
     */
    public void run(boolean useProxy) {
        this.useProxy = useProxy;
        this.run();
    }

    public static void runThread() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            LOGGER.info("lin start...");
            String trainDate = "2019-02-01,2019-02-02";
            String fromStation = "IZQ";
            String toStation = "FAQ";
            String trainNum = "D2985,D2959,D4707,D4285,D2951,G2901,D2809,D2367,D1801,D1867";
            trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
            String seats = "M,O,N";
            Shakedown12306Test s12306 = new Shakedown12306Test("18218798420", "qr_kh_6926641746");  // "18819458084", "llytest123"
            s12306.initQueryInfo(trainDate, fromStation, toStation, trainNum, seats);
            s12306.run(false);
            LOGGER.info("lin...");

        });
        LOGGER.info("--------------------");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.execute(() -> {
            LOGGER.info("liang start...");
            String trainDate = "2019-02-11";
            String fromStation = "FAQ";
            String toStation = "IZQ";
            String trainNum = "D4707,D2951,G2901,D2809,D2811,D1861,D2991,D1863,D2367,D1801,D1867,D1869,D2943,D2947,D2825";
            String seats = "M,O,N";
            Shakedown12306Test s12306 = new Shakedown12306Test("18819458084", "llytest123");  // "18819458084", "llytest123"
            s12306.initQueryInfo(trainDate, fromStation, toStation, trainNum, seats);
            s12306.run(false);
        });
    }

    public static void main(String[] args) {
//        Shakedown12306Test.runThread();

//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 2, 10, TimeUnit.SECONDS);

        String trainDate = "2019-02-01,2019-02-02";
        String fromStation = "IZQ";
        String toStation = "FAQ";
        String trainNum = "D2985,D2959,D4707,D4285,D2951,G2901,D2809,D2367,D1801,D1867";
        trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
        String seats = "M,O,N";
        Shakedown12306Test s12306 = new Shakedown12306Test("18218798420", "qr_kh_6926641746");  // "18819458084", "llytest123"
        s12306.initQueryInfo(trainDate, fromStation, toStation, trainNum, seats);
        s12306.run(false);
//        s12306.useProxy = true;
//        s12306.queryZAll();
    }

}
