package com.kalvin.ktools.comm.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kalvin.ktools.comm.kit.Shakedown12306Kit;
import com.kalvin.ktools.dto.User12306TicketOrderDTO;
import com.kalvin.ktools.service.Ticket12306OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 *
 * 第1位，表示秒，取值0-59
 * 第2位，表示分，取值0-59
 * 第3位，表示小时，取值0-23
 * 第4位，日期天/日，取值1-31
 * 第5位，日期月份，取值1-12
 * 第6位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思。另外：1表示星期天，2表示星期一。
 * 第7位，年份，可以留空，取值1970-2099
 *
 * (*)星号：可以理解为每的意思，每秒，每分，每天，每月，每年...
 * (?)问号：问号只能出现在日期和星期这两个位置，表示这个位置的值不确定，每天3点执行，所以第六位星期的位置，我们是不需要关注的，就是不确定的值。同时：日期和星期是两个相互排斥的元素，通过问号来表明不指定值。比如，1月10日，比如是星期1，如果在星期的位置是另指定星期二，就前后冲突矛盾了。
 * (-)减号：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12
 * (,)逗号：表达一个列表值，如在星期字段中使用“1,2,4”，则表示星期一，星期二，星期四
 * (/)斜杠：如：x/y，x是开始值，y是步长，比如在第一位（秒） 0/15就是，从0秒开始，每15秒，最后就是0，15，30，45，60    另：*\/y，等同于0/y
 *
 * 例子：
 * 0 0 3 * * ?     每天3点执行
 * 0 5 3 * * ?     每天3点5分执行
 * 0 5 3 ? * *     每天3点5分执行，与上面作用相同
 * 0 5/10 3 * * ?  每天3点的 5分，15分，25分，35分，45分，55分这几个时间点执行
 * 0 10 3 ? * 1    每周星期天，3点10分 执行，注：1表示星期天
 * 0 10 3 ? * 1#3  每个月的第三个星期，星期天 执行，#号只能出现在星期的位置
 */
@Component
@EnableScheduling
@ConditionalOnProperty(name = "kt.job.open", havingValue = "true")
public class Jobs {

    private final static Logger LOGGER = LoggerFactory.getLogger(Jobs.class);

    @Resource
    private Ticket12306OrderService ticket12306OrderService;

    @Value("${kt.shakedown.captcha.image.path}")
    private String captchaImagePath;

    private final String defaultEmail = "1481397688@qq.com";

    /**
     * 每天06:00启动所有抢票订单
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void startRobTickets() {
        LOGGER.info("每天06:00启动所有抢票订单...");
//        ticket12306OrderService.updateTicketStatusStartAllVail();
        this.runTickets();
    }

    /**
     * 检查抢票订单：每分钟检查一遍
     * 1、订单是否有效
     * 2、抢票状态是否正在进行
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void checkTicketOrder() {
        LOGGER.info("开始检查订单...");
        String s = DateUtil.formatTime(new Date());
        String[] split = s.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        if (hour > 5 && hour < 23) {    // 6点~23点之间
            if (hour == 6 && minute < 1) {
                return;
            }
            this.runTickets();
        }
    }

    /**
     * 每天23:00停止所有抢票订单
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void stopTakingTickets() {
        LOGGER.info("每天23:00停止所有抢票订单...");
        ticket12306OrderService.updateTicketStatusStopAllVail();
    }

    private void runTickets() {
        List<User12306TicketOrderDTO> ticketOrders = ticket12306OrderService.getAllValidAndStopTicketOrder();
        int size = ticketOrders.size();
        int maxPool = 10;   // 设置最大线程数10个
        if (size == 0) {
            return;
        }

        /*
         * 启动抢票状态已停止的订单
         * 使用定时线程调度。每隔一分钟启动一条线程
         */
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(size < maxPool ? size : maxPool);
        int scheduledTime = 0;  // 分钟
        for (User12306TicketOrderDTO o : ticketOrders) {

            // 更新抢票状态为抢票中
            ticket12306OrderService.updateTicketStatusStart(o.getId());

            ses.schedule(() -> {
                LOGGER.info("线程{}正在启动抢票订单{}", Thread.currentThread().getName(), o.getId());
                try {
                    Shakedown12306Kit.newInstance()
                            .initUser(o.getUsername(), o.getPassword())
                            .initQueryInfo(o.getTrainDate(), o.getFromStation(), o.getToStation(), o.getTrainNum(), o.getSeatType())
                            .initCaptchaImgPath(captchaImagePath)
                            .initBusiness(ticket12306OrderService, o.getId())
                            .initReceiver(StrUtil.isNotEmpty(o.getEmail()) ? o.getEmail() : defaultEmail)
                            .run();

                } catch (Exception e) {
                    LOGGER.info("线程{}启动抢票订单{}时发生了异常：{}", Thread.currentThread().getName(), o.getId(), e.getMessage());

                    // 更新的订单抢票状态为已停止
                    ticket12306OrderService.updateTicketStatusStop(o.getId());
                }
            }, scheduledTime, TimeUnit.MINUTES);

            scheduledTime++;
        }
    }

}
