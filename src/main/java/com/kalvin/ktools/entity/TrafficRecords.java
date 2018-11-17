package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 网站流量日志记录表
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
@TableName("kt_traffic_records")
public class TrafficRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 访客IP
     */
    private String ip;

    /**
     * 访客物理地址
     */
    private String mac;

    /**
     * 访客网络
     */
    private String isp;

    /**
     * 访客地理位置
     */
    private String gegraphicPos;

    /**
     * 访客请求url
     */
    private String reqUrl;

    /**
     * 请求url类型：0-页面 1-api
     */
    private Integer reqUrlType;

    /**
     * 请求方法
     */
    private String reqMethod;

    /**
     * 请求状态：0-成功 1-失败
     */
    private Integer reqStatus;

    /**
     * 请求耗时(ms)
     */
    private Integer reqTime;

    /**
     * 请求方式
     */
    private String reqType;

    /**
     * 请求时间
     */
    private LocalDateTime reqDatetime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getGegraphicPos() {
        return gegraphicPos;
    }

    public void setGegraphicPos(String gegraphicPos) {
        this.gegraphicPos = gegraphicPos;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public Integer getReqUrlType() {
        return reqUrlType;
    }

    public void setReqUrlType(Integer reqUrlType) {
        this.reqUrlType = reqUrlType;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public Integer getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(Integer reqStatus) {
        this.reqStatus = reqStatus;
    }

    public Integer getReqTime() {
        return reqTime;
    }

    public void setReqTime(Integer reqTime) {
        this.reqTime = reqTime;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public LocalDateTime getReqDatetime() {
        return reqDatetime;
    }

    public void setReqDatetime(LocalDateTime reqDatetime) {
        this.reqDatetime = reqDatetime;
    }

    @Override
    public String toString() {
        return "TrafficRecords{" +
        "id=" + id +
        ", ip=" + ip +
        ", mac=" + mac +
        ", isp=" + isp +
        ", gegraphicPos=" + gegraphicPos +
        ", reqUrl=" + reqUrl +
        ", reqUrlType=" + reqUrlType +
        ", reqMethod=" + reqMethod +
        ", reqStatus=" + reqStatus +
        ", reqTime=" + reqTime +
        ", reqType=" + reqType +
        ", reqDatetime=" + reqDatetime +
        "}";
    }
}
