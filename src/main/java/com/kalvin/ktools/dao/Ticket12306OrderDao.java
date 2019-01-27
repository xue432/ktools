package com.kalvin.ktools.dao;

import com.kalvin.ktools.dto.User12306TicketOrderDTO;
import com.kalvin.ktools.entity.Ticket12306Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 12306抢票订单信息表 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-27
 */
public interface Ticket12306OrderDao extends BaseMapper<Ticket12306Order> {

    /**
     * 查询12306账户所有订单状态正常且抢票状态已停止的抢票订单
     * @return list
     */
    List<User12306TicketOrderDTO> selectAllValidAndStopTicketOrder();

}
