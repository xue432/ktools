package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 12306抢票任务信息表
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-26
 */
@TableName("kt_ticket12306_task")
public class Ticket12306Task implements Serializable {

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
     * 座席类型。M：一等座；O：二等座；N：无座。多个用英文逗号分隔
     */
    private String seatType;

    /**
     * 抢票状态：0：已停止；1：已开启
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

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
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
        return "Ticket12306Task{" +
        "id=" + id +
        ", userId=" + userId +
        ", trainDate=" + trainDate +
        ", trainNum=" + trainNum +
        ", seatType=" + seatType +
        ", status=" + status +
        ", createTime=" + createTime +
        "}";
    }
}
