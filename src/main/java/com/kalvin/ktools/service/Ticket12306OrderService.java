package com.kalvin.ktools.service;

import com.kalvin.ktools.dto.User12306TicketOrderDTO;
import com.kalvin.ktools.entity.Ticket12306Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 12306抢票订单信息表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-27
 */
public interface Ticket12306OrderService extends IService<Ticket12306Order> {

    /**
     * 获取12306账户所有订单状态正常且抢票状态已停止的抢票订单
     * @return list
     */
    List<User12306TicketOrderDTO> getAllValidAndStopTicketOrder();

    /**
     * 根据订单ID更新抢票状态为进行中
     */
    void updateTicketStatusStart(Long id);

    /**
     * 根据订单ID更新抢票状态为已停止
     */
    void updateTicketStatusStop(Long id);

    /**
     * 更新所有正常状态订单的抢票状态为进行中
     */
    void updateTicketStatusStartAllVail();

    /**
     * 更新所有正常状态订单的抢票状态为已停止
     */
    void updateTicketStatusStopAllVail();

}
