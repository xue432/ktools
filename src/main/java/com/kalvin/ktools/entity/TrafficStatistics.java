package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网站流量统计表
 * </p>
 *
 * @author Kalvin
 * @since 2018-11-16
 */
@TableName("kt_traffic_statistics")
public class TrafficStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访客IP(主键)
     */
    @TableId(type = IdType.INPUT)
    private String ip;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 访客地理位置
     */
    private String gegraphicPos;

    /**
     * 页面访问总次数
     */
    private Integer pageVisitTimes;

    /**
     * api访问总次数
     */
    private Integer apiVisitTimes;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGegraphicPos() {
        return gegraphicPos;
    }

    public void setGegraphicPos(String gegraphicPos) {
        this.gegraphicPos = gegraphicPos;
    }

    public Integer getPageVisitTimes() {
        return pageVisitTimes;
    }

    public void setPageVisitTimes(Integer pageVisitTimes) {
        this.pageVisitTimes = pageVisitTimes;
    }

    public Integer getApiVisitTimes() {
        return apiVisitTimes;
    }

    public void setApiVisitTimes(Integer apiVisitTimes) {
        this.apiVisitTimes = apiVisitTimes;
    }

    @Override
    public String toString() {
        return "TrafficStatistics{" +
                "ip='" + ip + '\'' +
                ", userId=" + userId +
                ", gegraphicPos='" + gegraphicPos + '\'' +
                ", pageVisitTimes=" + pageVisitTimes +
                ", apiVisitTimes=" + apiVisitTimes +
                '}';
    }
}
