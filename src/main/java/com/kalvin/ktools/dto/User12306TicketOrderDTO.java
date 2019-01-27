package com.kalvin.ktools.dto;

import com.kalvin.ktools.entity.Ticket12306Order;

/**
 * 12306用户抢票订单实体
 */
public class User12306TicketOrderDTO extends Ticket12306Order {

    /**
     * 12306账号
     */
    private String username;

    /**
     * 12306账号密码
     */
    private String password;

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

    @Override
    public String toString() {
        return "User12306TicketOrderDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                "'},'" + super.toString();
    }
}
