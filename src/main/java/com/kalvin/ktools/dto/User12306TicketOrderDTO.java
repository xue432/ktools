package com.kalvin.ktools.dto;

import com.kalvin.ktools.entity.Ticket12306Order;

/**
 * 12306用户抢票订单实体
 */
public class User12306TicketOrderDTO extends Ticket12306Order {

    private static final long serialVersionUID = 9157361478788673907L;

    /**
     * 12306账号
     */
    private String username;

    /**
     * 12306账号密码
     */
    private String password;

    /**
     * 邮箱地址
     */
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User12306TicketOrderDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
