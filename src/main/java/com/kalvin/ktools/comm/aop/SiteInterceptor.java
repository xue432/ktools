package com.kalvin.ktools.comm.aop;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.comm.kit.HttpServletContextKit;
import com.kalvin.ktools.comm.kit.KToolkit;
import com.kalvin.ktools.entity.TrafficRecords;
import com.kalvin.ktools.entity.TrafficStatistics;
import com.kalvin.ktools.service.TrafficRecordsService;
import com.kalvin.ktools.service.TrafficStatisticsService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 网站统计拦截器
 */
@Aspect
@Component
public class SiteInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(SiteInterceptor.class);

    @Resource
    private TrafficRecordsService recordsService;

    @Resource
    private TrafficStatisticsService statisticsService;

    @Pointcut("@annotation(com.kalvin.ktools.comm.annotation.SiteStats)")
    public void siteStats() { }

    @Pointcut("execution(* com.kalvin.ktools.controller..*.*(..))")
    public void all() { }

    @Before(value = "all()")
    public void before() {
        HttpServletRequest request = HttpServletContextKit.getHttpServletRequest();
        String clientIP = HttpUtil.getClientIP(request);
        clientIP = "0:0:0:0:0:0:0:1".equals(clientIP) ? "127.0.0.1" : clientIP;
        LOGGER.info("{}正在请求:{}", clientIP, request.getContextPath() + request.getRequestURI());
    }

    /*@After(value = "siteStats()")
    public void after(JoinPoint joinPoint) {
        LOGGER.info("after.............");
    }*/

    /*@AfterReturning(value = "siteStats()")
    public void afterReturn(JoinPoint joinPoint) {
        LOGGER.info("afterReturn.............");
    }*/

    @AfterThrowing(value = "siteStats()")
    public void afterThrowing(JoinPoint joinPoint) {
        // 记录请求信息
        TrafficRecords reqInfo = this.getReqInfo(joinPoint);
        reqInfo.setReqStatus(1);    // 设置请求状态:失败
        recordsService.save(reqInfo);

        addOrVisitTimesUp(reqInfo);
    }

    @Around(value = "siteStats()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        DateTime startTime = DateUtil.date();
        Object proceed = pjp.proceed();
        DateTime endTime = DateUtil.date();
        long dft = DateUtil.betweenMs(startTime, endTime);  // 请求耗时（ms）
        LOGGER.info("请求耗时：{}ms", dft);

        // 记录请求信息
        TrafficRecords reqInfo = this.getReqInfo(pjp);
        reqInfo.setReqTime((int) dft);
        recordsService.save(reqInfo);

        addOrVisitTimesUp(reqInfo);

        return proceed;
    }

    /**
     * 新增或更新访客ip访问网站页面次数
     * @param reqInfo TrafficRecords
     */
    private void addOrVisitTimesUp(TrafficRecords reqInfo) {
        TrafficStatistics statistics = statisticsService.getById(reqInfo.getIp());
        if (statistics == null) {
            statistics = new TrafficStatistics();
            statistics.setIp(reqInfo.getIp());
            statistics.setGegraphicPos(reqInfo.getGegraphicPos());
            if (Constant.REQ_URL_TYPE_PAGE == reqInfo.getReqUrlType()) {
                statistics.setPageVisitTimes(1);
            } else {
                statistics.setApiVisitTimes(1);
            }
        } else {    // 访问次数加1
            if (Constant.REQ_URL_TYPE_PAGE == reqInfo.getReqUrlType()) {
                statistics.setPageVisitTimes(statistics.getPageVisitTimes() + 1);
            } else {
                statistics.setApiVisitTimes(statistics.getApiVisitTimes() + 1);
            }
        }
        statisticsService.saveOrUpdate(statistics);
    }

    /**
     * 获取请求信息
     * @param pjp JoinPoint
     */
    private TrafficRecords getReqInfo(JoinPoint pjp) {
        HttpServletRequest request = HttpServletContextKit.getHttpServletRequest();
        String ip = HttpUtil.getClientIP(request);

        // 获取IP信息
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        String url = request.getRequestURI();
        String method = request.getMethod();
        String ipInfo = KToolkit.getIPInfo(ip);   // ip信息
        String address = "";
        String isp = "";
        if (ipInfo != null) {
            address = ipInfo.split(" ")[0];
            isp = ipInfo.split(" ")[1];
        }
//        LOGGER.info("ip={}, url={}, method={}, address={}, isp={}", ip, url, method, address, isp);

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class returnType = signature.getReturnType();
        String name = signature.getName();  // 请求方法名
        int reqUrlType;
        if (returnType == ModelAndView.class) {
            reqUrlType = 0;
        } else {
            reqUrlType = 1;
        }
        TrafficRecords records = new TrafficRecords();
        records.setIp(ip);
        records.setMac("00-00-00-00-00-00");    // todo 暂时写死的
        records.setGegraphicPos(address);
        records.setIsp(isp);
        records.setReqMethod(name);
        records.setReqType(method);
        records.setReqUrl(url);
        records.setReqUrlType(reqUrlType);
        return records;
    }

}
