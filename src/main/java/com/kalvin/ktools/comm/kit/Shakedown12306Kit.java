package com.kalvin.ktools.comm.kit;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.ktools.service.Ticket12306OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
public class Shakedown12306Kit {

    private final static Logger LOGGER = LoggerFactory.getLogger(Shakedown12306Kit.class);

    private HttpRequest request12306;

    private Ticket12306OrderService ticketOrderService;

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

    private String captchaImagePath;

    private String loginCaptchaImageName;   // 登录验证码图片名称
    private String orderCaptchaImageName;   // 提交订单验证码图片名称

    /*乘客订票相关参数*/
    private String trainDates;
    private String fromStation;
    private String toStation;
    private String trainNums;
    private String seats;

    private String answer;
    private String tk;
    private String cookie;

    /* 下订单相关参数 */
    private String queryFromStationName;    // 查询始发站
    private String queryToStationName;  // 查询到达站

    private String secretStr;   // 车次加密串
    private String trainDate;   // 列车出发日期
    private String formStationName; //  始发站
    private String toStationName;   // 到达站
    private String seatType;    // 座席类型：M、O(二等座、无座)

    private String repeatSubmitToken; // 请求提交令牌

    private String trainNo;
    private String trainNum;
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

    private final List<String> dictCode = Arrays.asList("36,46", "116,46", "188,46", "267,43", "40,118", "113,119", "190,122", "264,115");

    private int btn;    // 刷票次数

    private MyCache blackRoom = new MyCache();  // 小黑屋；临时存放排除失败的列车班次。5分钟后重置

    private String receiver;   // 邮件收件人

    private Long orderId;   // 订单ID

    public static Shakedown12306Kit newInstance() {
        return new Shakedown12306Kit();
    }

    public Shakedown12306Kit initUser(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * 初始化订票参数信息
     * @param trainDates 列车出发日期
     * @param fromStation 始发站
     * @param toStation 终点站
     * @param trainNums 列表车次
     * @param seats 座席类型：M、O、N
     */
    public Shakedown12306Kit initQueryInfo(String trainDates, String fromStation, String toStation, String trainNums, String seats) {
        this.trainDates = trainDates;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.trainNums = trainNums;
        this.seats = seats;
        return this;
    }

    public Shakedown12306Kit initCaptchaImgPath(String path) {
        this.captchaImagePath = path;
        return this;
    }

    public Shakedown12306Kit initReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Shakedown12306Kit initBusiness(Ticket12306OrderService service, Long orderId) {
        this.ticketOrderService = service;
        this.orderId = orderId;
        return this;
    }

    public Shakedown12306Kit initPassenger(String passenger, String passengerIdNo) {
        this.passengerTicketStr = this.passengerTicketStr.replace("{username}", passenger).replace("{passengerIdCard}", passengerIdNo);
        this.oldPassengerStr = this.oldPassengerStr.replace("{username}", passenger).replace("{passengerIdCard}", passengerIdNo);
        return this;
    }

    public void reset() {
        this.tk = "";
        this.cookie = "";
    }

    /**
     * 进入登录页
     */
    private void loginInit() {
        this.stopCancelOrderThread();
        this.reset();

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

    }

    /**
     * 下载图片验证码
     * @param type 图片验证码类型
     */
    private void getCaptchaImage(CaptchaImageType type) {
        String params;
        this.request12306.setMethod(Method.GET);

        this.loginCaptchaImageName = "login" + RandomUtil.randomString(5) + ".png";
        this.orderCaptchaImageName = "order" + RandomUtil.randomString(5) + ".png";
        if (type.value == 0) {
            params = "?login_site=E&module=login&rand=sjrand&" + this.genDoubleRand();
            this.request12306.setUrl(captcha + params);
            HttpResponse httpResponse = this.request12306.executeAsync();
            httpResponse.writeBody(new File(captchaImagePath + this.loginCaptchaImageName));
        }
        if (type.value == 1) {
            params = "?module=passenger&rand=randp&" + this.genDoubleRand();
            this.request12306.setUrl(captcha + params);
            HttpResponse httpResponse = this.request12306.executeAsync();
            httpResponse.writeBody(new File(captchaImagePath + this.orderCaptchaImageName));
        }
    }

    /**
     * 验证登录图片验证码
     * @return bl
     */
    private boolean authLoginCaptchaImage() {
        /*Scanner scanner = new Scanner(System.in);
        String myCodes = scanner.nextLine();*/

        this.sleep(2000);
        String autoCode = new ImageAI().autoDELPHIl12306(captchaImagePath + this.loginCaptchaImageName);
        this.answer = this.getCaptchaCode(autoCode);
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
            LOGGER.info("登录失败，请检查12306账号或密码是否正确。");
            throw new RuntimeException("登录失败，请检查12306账号或密码是否正确。");
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
            this.tk = (String) jsonObject.get("newapptk");
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

        LOGGER.info("token={}", tk);
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
        LOGGER.info("账号={},name={},idTypeCode={},idNo={}", this.username, name, idTypeCode, idNo);
        this.passengerTicketStr = this.passengerTicketStr.replace("{username}", name).replace("{passengerIdCard}", idNo);
        this.oldPassengerStr = this.oldPassengerStr.replace("{username}", name).replace("{passengerIdCard}", idNo);
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
        String time = DateUtil.formatTime(new Date());  // 14:47:56
        String[] ts = time.split(":");
        int hour = Integer.valueOf(ts[0]);
        int minute = Integer.valueOf(ts[1]);
        if (hour >= 23 && minute >= 0) {
            throw new RuntimeException("每天23点后抛异常终止当前抢票线程" + Thread.currentThread().getName());
        }

        this.stopCancelOrderThread();

        String[] split = this.trainDates.split(",");
        int len = split.length;
        int i = 0;
        for (String trainDate : split) {
            i++;
            String params = "?leftTicketDTO.train_date="+trainDate+"&leftTicketDTO.from_station="+this.fromStation+"&leftTicketDTO.to_station="+this.toStation+"&purpose_codes=ADULT";
            if (this.queryZ(params, trainDate)) {
                return true;
            }
            if (i < len) this.sleep(500);
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

            HttpResponse execute = this.request12306.execute();
            String body = execute.body();
//            LOGGER.info("query status = {}，body={}", execute.getStatus(), body);

            List<TrainSchedule> scheduleList = this.handleTrainInfo(body);

            for (TrainSchedule ts : scheduleList) {
                boolean submitFlg = false;
                String trainNum = ts.getTrainNum();
                String l1Seat = ts.getL1Seat();
                String l2Seat = ts.getL2Seat();
                String noSeat = ts.getNoSeat();

                // 判断当前车次是否在小黑屋中，若在，跳过此车次
                if (this.blackRoom.get(trainNum) != null) {
                    break;
                }

                LOGGER.info("可预订车票：发车日期：{}，车次：{}，出发时间：{}，到达时间：{}，座席：一等座{}、二等座{}、无座{}", trainDate, trainNum, ts.getGoOffTime(), ts.getArrivalTime(), l1Seat, l2Seat, noSeat);
                if (this.trainNums.contains(trainNum)) { // 当前车次有票

                    // 先进行一次解码。避免提交后再编码一次导致参数失效
                    this.secretStr = this.urlDecode(ts.getSecretStr());
                    this.leftTicket = this.urlDecode(ts.getLeftTicket());

                    this.trainNo = ts.getTrainNo();
                    this.trainNum = trainNum;
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
                            throw new RuntimeException("没有权限。请重新登录");
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("查询订单异常：{}", e.getMessage());
        }

        this.btn++;
        LOGGER.info("-------线程{}已为账号{}刷票{}次--------", Thread.currentThread().getName(), this.username, btn);
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
    private boolean submitOrderRequest(int submitTime) {

        this.checkUser();

        LOGGER.info("secretStr={},trainDate={},formStationName={},toStationName={}",
                secretStr, trainDate, this.queryFromStationName, this.queryToStationName);

        this.request12306.setUrl(submitOrderRequest);
        this.request12306.setMethod(Method.POST);

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

        HttpResponse execute = this.request12306.execute();
        try {
            String body = execute.body();
            LOGGER.info("submitOrderRequest={}", body);
            JSONObject jsonObject = JSONUtil.parseObj(body);
            boolean status = (boolean) jsonObject.get("status");

            if (status) {
                this.initDc();
                String data = jsonObject.get("data").toString();
                if ("N".equals(data)) {
                    return true;
                }
            } else {
                isReSubmit = true;
                LOGGER.info("订单提交失败，信息：{}", jsonObject.get("messages"));
            }
        } catch (Exception e) {
            isReSubmit = true;
        }

        if (isReSubmit) {
            LOGGER.info("submitOrder status:{}", execute.getStatus());
            if (submitTime < 2) {   // 重试预提交订单两次
                submitTime++;
                return this.submitOrderRequest(submitTime);
            }
        }
        return false;
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

        LOGGER.info("checkOrderInfo={}", this.passengerTicketStr);
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

    }

    /**
     * 获取订单队列位置
     */
    private boolean getQueueCount() {
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

        try {
            HttpResponse execute = this.request12306.execute();
            String body = execute.body();
            LOGGER.info("getQueueCount body = {}", body);
            // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"count":"3","ticket":"0,154","op_2":"false","countT":"0","op_1":"true"},"messages":[],"validateMessages":{}}

            JSONObject object = JSONUtil.parseObj(body);
            JSONObject dataObj = (JSONObject) object.get("data");
            if ((boolean) object.get("status")) {
                String ticket = dataObj.get("ticket").toString();
                Integer count = Integer.valueOf(dataObj.get("count").toString());   // todo 到底是这个是排队位置还是countT 我这里使用count
                Integer countT = Integer.valueOf(dataObj.get("countT").toString());
                LOGGER.info("countT={}", countT);

                if (!ticket.contains(",")) {
                    this.ticketCount = Integer.valueOf(ticket);
                } else {
                    String[] ticketSplit = ticket.split(",");
                    this.ticketCount = Arrays.stream(ticketSplit).map(Integer::valueOf).reduce(0, Integer::sum);
//                    this.ticketCount = Arrays.stream(ticketSplit).map(Integer::valueOf).max(Integer::compareTo).get();
                }

                LOGGER.info("进入排队成功，你排在：{}位，当前余票还有：{}张", count, ticketCount);
                return true;
            } else {
                LOGGER.info("准备进入排队错误，错误信息：{}", object.get("messages"));
            }
        } catch (Exception e) {
            LOGGER.info("准备进入排队错误：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 真正下订单
     */
    private boolean confirmSingleForQueue() {

        String randCode = "";
        if (this.isNeedCode) {
            LOGGER.info("需要订单验证码，正在打印验证码...");
//            Scanner scanner = new Scanner(System.in);
//            String myCodes = scanner.nextLine();

            String autoCode;
            try {
                autoCode = new ImageAI().autoDELPHIl12306(captchaImagePath + this.orderCaptchaImageName);
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
                return false;
            }
            randCode = this.getCaptchaCode(autoCode);
            LOGGER.info("randCode={}", randCode);

            /*for (int i = 0; i < 3; i++) {
                String autoCode = ImageAI.autoDELPHIl12306(captchaImagePath + this.orderCaptchaImageName);
                randCode = this.getCaptchaCode(autoCode);
                LOGGER.info("randCode={}", randCode);
                // todo 验证订单验证码 /otn/passcodeNew/checkRandCodeAnsyn
                // "randCode": self.randCode,
                // "rand": "randp",
                // "_json_att": "",
                // "REPEAT_SUBMIT_TOKEN": self.token
                break;
            }*/

        } else {
            LOGGER.info("不需要订单验证码，直接提交");
        }

        LOGGER.info("ifShowPassCodeTime = {}", this.ifShowPassCodeTime);
        this.sleep(this.ifShowPassCodeTime);   // 睡眠

        this.request12306.setUrl(confirmSingleForQueue);
        this.request12306.setMethod(Method.POST);
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");

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

        try {
            HttpResponse execute = this.request12306.execute();
            String body = execute.body();
            LOGGER.info("confirmSingleForQueue body = {}", body);
            // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"submitStatus":true},"messages":[],"validateMessages":{}}

            final JSON parse = JSONUtil.parse(body);
            if ((boolean) parse.getByPath("status")) {
                if ((boolean) parse.getByPath("data.submitStatus")) {
                    return true;
                } else {
                    LOGGER.info("正式下单失败，{}。车次{}加入小黑屋", parse.getByPath("data.errMsg"), this.trainNum);
                    this.blackRoom.put(this.trainNum, this.trainNum, 3 * 60);
                }
            } else {
                LOGGER.info("正式下单失败，{}", parse.getByPath("messages"));
            }
        } catch (Exception e) {
            LOGGER.info("正式下单发生异常：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 排队获取订单等待信息,每隔3秒请求一次，最高请求次数为20次！
     */
    private boolean queryOrderWaitTime() {
        this.request12306.setMethod(Method.GET);
        this.request12306.header("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");

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
                        // 订票成功，使用邮件通知抢票人
                        LOGGER.info("恭喜您订票成功，订单号为：{}, 请立即打开浏览器登录12306，访问‘未完成订单’，在30分钟内完成支付!", orderId);
                        MailUtil.sendText(this.receiver, "12306抢票成功",
                                "恭喜您订票成功，订单号为：" + orderId + ", 请立即打开浏览器登录12306，访问‘未完成订单’，在30分钟内完成支付!");
                        return true;
                    } else if (StrUtil.isEmpty(dObj.get("msg").toString())) {
                        LOGGER.info("正在排队，排队时间剩余：{}毫秒", dObj.get("waitTime"));
                    } else {
                        LOGGER.info("排队失败：{}", dObj.get("msg"));
                        break;
                    }
                } else {
                    LOGGER.info("排队失败，{}", parse.getByPath("messages"));
                }

                tryTimes++;
                LOGGER.info("第{}次排队中...", tryTimes);
            } catch (Exception e) {
                LOGGER.error("排队发生异常：{}", e.getMessage());
                break;
            }
            this.sleep(2000);   // 睡眠2秒
        }

        LOGGER.info("车次{}加入小黑屋，闭关3分钟", this.trainNum);
        this.blackRoom.put(this.trainNum, this.trainNum, 3 * 60);
        return false;
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
     * @param myCodes 1~8
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

    /**
     * 终止已取消的订单线程
     */
    private void stopCancelOrderThread() {
        if (this.ticketOrderService != null && this.orderId != null){
            if (this.ticketOrderService.isCancel(this.orderId)) {
                throw new RuntimeException("抢票订单已取消，终止当前抢票线程" + Thread.currentThread().getName());
            }
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
     * 自定义缓存类
     */
    class MyCache {

        private HashMap<String, Object> cacheMap = new HashMap<>();
        private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        public void put(String key, Object value) {
            this.put(key, value, 0);
        }

        public synchronized void put(String key, Object value, long expire) {

            if (expire > 0) {   // 过期缓存
                scheduledExecutorService.schedule(() -> {
                    this.cacheMap.remove(key);
                }, expire, TimeUnit.SECONDS);

                this.cacheMap.put(key, value);
            } else {    // 不过期缓存
                this.cacheMap.put(key, value);
            }
        }

        public Object get(String key) {
            return this.cacheMap.get(key);
        }

        public int size() {
            return this.cacheMap.size();
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
     * 图片AI
     */
    static class ImageAI {

        private int reTime = 0;

        /**
         * 自动识别12306图片验证码
         * @param imgSrc 验证码图片路径
         * @return 验证码位置数字：12345678
         */
        public String autoDELPHIl12306(String imgSrc) {
            try {
                HttpRequest post = HttpUtil.createPost("http://littlebigluo.qicp.net:47720/");
                post.form("pic_xxfile", new File(imgSrc));
                String body = post.execute().body();
                List<String> list = ReUtil.findAll("<B>(.*?)</B>", body, 1);
                LOGGER.info("自动识别12306图片验证码：{}", list.get(0));
                return list.get(0).replaceAll(" ", "");
            } catch (Exception e) {
                if (reTime > 20) {
                    throw new RuntimeException("自动识别12306图片验证码超出重试次数，程序已终止");
                }
                reTime++;
                LOGGER.info("自动识别12306图片验证码异常，正在第{}次重试中...", reTime);
                try {
                    Thread.sleep(2000);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return autoDELPHIl12306(imgSrc);
            }
        }
    }

    /**
     * 开始抢票。不使用代理IP
     */
    public void run() throws Exception {
        // 1-进入12306登录页
        loginInit();
        // 2-获取登录图片验证码
        this.getCaptchaImage(CaptchaImageType.LOGIN);
        int submitTime = 0;
        // 3-验证图片验证码
        if (this.authLoginCaptchaImage()) {
            // 4-登录12306
            this.login();
            // 5-进入查票页面
            this.initTicket();
            // 6-查票
            while (!this.queryZAll()) {
                this.sleep(2000);
            }
            // 7-预提交订单
            if (!this.submitOrderRequest(submitTime)) {
                LOGGER.info("预提交订单失败，正在重新登录...");
                this.run();
                return;
            }
            // 8-检查订单的正确性
            this.checkOrderInfo();
            // 9-准备进入排队
            if (!this.getQueueCount()) {
                LOGGER.info("准备进入排队失败，正在重新登录...");
                this.run();
                return;
            }
            // 10-正式下单
            if (!this.confirmSingleForQueue()) {
                LOGGER.info("正式下单失败，正在重新登录...");
                this.run();
                return;
            }
            // 11-排队等待
            if (!this.queryOrderWaitTime()) {
                LOGGER.info("排队失败，正在重新登录...");
                // 排队失败，发送邮件告知抢票人
                MailUtil.sendText(this.receiver, "抢票程序排队失败", this.trainNum + "车次排队失败。");
                this.run();
            }
        } else {
            LOGGER.info("验证验证码不通过。正在重新登录...");
            this.run();
        }
    }

    /**
     * 开始抢票。使用代理IP
     * @param useProxy 是否开启代理IP
     */
    public void run(boolean useProxy) throws Exception {
        this.useProxy = useProxy;
        this.run();
    }

}
