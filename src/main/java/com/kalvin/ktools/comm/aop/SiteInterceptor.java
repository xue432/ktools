package com.kalvin.ktools.comm.aop;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.kit.HttpServletContextKit;
import com.kalvin.ktools.comm.kit.KToolkit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 网站统计拦截器
 */
@Aspect
@Component
public class SiteInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(SiteInterceptor.class);

//    private static long time = 0;   // 请求耗时

    @Pointcut("@annotation(com.kalvin.ktools.comm.annotation.SiteStats)")
    public void siteStats() { }

    @Pointcut("execution(* com.kalvin.ktools.controller..*.*(..))")
    public void all() { }

    @Before(value = "all()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest request = HttpServletContextKit.getHttpServletRequest();
        String clientIP = HttpUtil.getClientIP(request);
        clientIP = "0:0:0:0:0:0:0:1".equals(clientIP) ? "127.0.0.1" : clientIP;
        LOGGER.info("{}正在请求:{}", clientIP, clientIP + request.getRequestURI());
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
        LOGGER.info("afterThrowing.............");
        Integer reqStatus = 1;  // 请求状态:失败
        this.getReqInfo(joinPoint);
    }

    @Around(value = "siteStats()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info("start around.............");

        DateTime startTime = DateUtil.date();
        Object proceed = pjp.proceed();
        DateTime endTime = DateUtil.date();
        long dft = DateUtil.betweenMs(startTime, endTime);  // 请求耗时（ms）
        LOGGER.info("请求耗时：{}ms", dft);
        this.getReqInfo(pjp);
        return proceed;
    }

    /**
     * 获取请求信息
     * @param pjp JoinPoint
     */
    private void getReqInfo(JoinPoint pjp) {
        HttpServletRequest request = HttpServletContextKit.getHttpServletRequest();
        String ip = HttpUtil.getClientIP(request);

        // 获取IP信息
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        String url = request.getRequestURI();
        String method = request.getMethod();
        String ipInfo = KToolkit.getIPInfo(ip);   // ip信息
        String address = ipInfo.split(" ")[0];
        String isp = ipInfo.split(" ")[1];
        LOGGER.info("ip={}, url={}, method={}, address={}, isp={}", ip, url, method, address, isp);

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class returnType = signature.getReturnType();
        String name = signature.getName();  // 请求方法名
        int reqUrlType;
        if (returnType == ModelAndView.class) {
            reqUrlType = 0;
            LOGGER.info("request page.. page_visit_times++");
        } else {
            reqUrlType = 1;
            LOGGER.info("request api.. api_visit_times++");
        }
        LOGGER.info("name={},reqUrlType={},returnType={}", name, reqUrlType, returnType);
    }

}
