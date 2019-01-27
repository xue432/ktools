package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.BaseTest;
import com.kalvin.ktools.dto.User12306TicketOrderDTO;
import com.kalvin.ktools.service.Ticket12306OrderService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class Ticket12306OrderServiceImplTest extends BaseTest {

    @Resource
    private Ticket12306OrderService service;

    @Test
    public void getAllValidAndStopTicketOrder() {
        service.getAllValidAndStopTicketOrder().forEach(System.out::println);
    }
}