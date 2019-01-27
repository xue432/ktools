package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 12306抢票订单信息表
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-27
 */
@TableName("kt_ticket12306_order")
public class Ticket12306Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user12306表ID
     */
    private Long userId;

    /**
     * 乘客出发日期，多个使用英文逗号分隔
     */
    private String trainDate;

    /**
     * 车次，多个使用英文逗号分隔
     */
    private String trainNum;

    /**
     * 始发站
     */
    private String fromStation;

    /**
     * 终点站
     */
    private String toStation;

    /**
     * 座席类型。M：一等座；O：二等座；N：无座。多个用英文逗号分隔
     */
    private String seatType;

    /**
     * 抢票状态。0：已停止；1：进行中
     */
    private Integer ticketStatus;

    /**
     * 订单状态：0：正常；1：已取消
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Ticket12306Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", trainDate='" + trainDate + '\'' +
                ", trainNum='" + trainNum + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", seatType='" + seatType + '\'' +
                ", ticketStatus=" + ticketStatus +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
