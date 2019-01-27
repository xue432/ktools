package com.kalvin.ktools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.dao.Ticket12306OrderDao;
import com.kalvin.ktools.dto.User12306TicketOrderDTO;
import com.kalvin.ktools.entity.Ticket12306Order;
import com.kalvin.ktools.service.Ticket12306OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 12306抢票订单信息表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-27
 */
@Service
public class Ticket12306OrderServiceImpl extends ServiceImpl<Ticket12306OrderDao, Ticket12306Order> implements Ticket12306OrderService {

    @Override
    public List<User12306TicketOrderDTO> getAllValidAndStopTicketOrder() {
        return baseMapper.selectAllValidAndStopTicketOrder();
    }

    @Override
    public void updateTicketStatusStart(Long id) {
        Ticket12306Order ticket12306Order = new Ticket12306Order();
        ticket12306Order.setId(id);
        ticket12306Order.setTicketStatus(Constant.TICKET_STATUS_START);
        updateById(ticket12306Order);
    }

    @Override
    public void updateTicketStatusStop(Long id) {
        Ticket12306Order ticket12306Order = new Ticket12306Order();
        ticket12306Order.setId(id);
        ticket12306Order.setTicketStatus(Constant.TICKE_TSTATUS_STOP);
        updateById(ticket12306Order);
    }

    @Override
    public void updateTicketStatusStartAllVail() {
        Ticket12306Order ticket12306Order = new Ticket12306Order();
        ticket12306Order.setTicketStatus(Constant.TICKET_STATUS_START);
        LambdaQueryWrapper<Ticket12306Order> lambdaQueryWrapper = new QueryWrapper<Ticket12306Order>()
                .lambda().eq(Ticket12306Order::getStatus, Constant.STATUS_0);
        update(ticket12306Order, lambdaQueryWrapper);
    }

    @Override
    public void updateTicketStatusStopAllVail() {
        Ticket12306Order ticket12306Order = new Ticket12306Order();
        ticket12306Order.setTicketStatus(Constant.TICKE_TSTATUS_STOP);
        LambdaQueryWrapper<Ticket12306Order> lambdaQueryWrapper = new QueryWrapper<Ticket12306Order>()
                .lambda().eq(Ticket12306Order::getStatus, Constant.STATUS_0);
        update(ticket12306Order, lambdaQueryWrapper);
    }
}
