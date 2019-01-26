package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 12306账号信息表
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-26
 */
@TableName("kt_user12306")
public class User12306 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 12306账号
     */
    private String username;

    /**
     * 12306账号密码
     */
    private String password;

    /**
     * 乘客身份证号
     */
    private String passengerIdNo;

    /**
     * 乘客姓名
     */
    private String passenger;

    /**
     * 成功出票次数
     */
    private Integer ticketNum;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public User12306(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassengerIdNo() {
        return passengerIdNo;
    }

    public void setPassengerIdNo(String passengerIdNo) {
        this.passengerIdNo = passengerIdNo;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public Integer getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(Integer ticketNum) {
        this.ticketNum = ticketNum;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User12306{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", passengerIdNo=" + passengerIdNo +
        ", passenger=" + passenger +
        ", ticketNum=" + ticketNum +
        ", createTime=" + createTime +
        "}";
    }
}
