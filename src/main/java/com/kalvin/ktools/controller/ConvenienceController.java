package com.kalvin.ktools.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.RandomUtil;
import com.kalvin.ktools.comm.annotation.SiteStats;
import com.kalvin.ktools.comm.kit.KToolkit;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.entity.Ticket12306Order;
import com.kalvin.ktools.entity.User12306;
import com.kalvin.ktools.service.Ticket12306OrderService;
import com.kalvin.ktools.service.User12306Service;
import com.kalvin.ktools.vo.ShakedownVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 便民工具 控制层
 */
@RestController
@RequestMapping(value = "convenience")
public class ConvenienceController {

    @Value(value = "${kt.image.handle.dir}")
    private String imageHandleDir;

    @Autowired
    private User12306Service user12306Service;

    @Autowired
    private Ticket12306OrderService ticket12306OrderService;

    @SiteStats
    @GetMapping(value = "qrCode")

    public ModelAndView qrCode() {
        return new ModelAndView("convenience/qr_code.html");
    }

    @SiteStats
    @GetMapping(value = "ip")
    public ModelAndView ip() {
        return new ModelAndView("convenience/ip.html");
    }

    @SiteStats
    @GetMapping(value = "shakedown12306")
    public ModelAndView shakedown12306() {
        return new ModelAndView("convenience/shakedown12306.html");
    }

    @SiteStats
    @PostMapping(value = "qrCode/2Image")
    public R qrCode2Image(String base64) {
        BufferedImage bufferedImage = ImageUtil.toImage(base64);
        try {
            String fileName = DateUtil.format(new Date(), "yyMMddhhmmss") + "_" +
                    RandomUtil.randomString(3) + ".png";
            ImageIO.write(bufferedImage, "png", new File(imageHandleDir + fileName));
            return R.ok(fileName);
        } catch (IOException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取IP信息
     *
     * @param ip ip地址
     * @return r
     */
    @SiteStats
    @GetMapping(value = "get/ipInfo")
    public R getIpInfo(String ip) {
        return R.ok(KToolkit.getIPInfo(ip));
    }

    @SiteStats
    @PostMapping(value = "shakedown")
    public R shakedown(ShakedownVO shakedownVO) {

        // 保存12306账号信息
        User12306 user12306 = user12306Service.saveUser12306(shakedownVO.getUsername(), shakedownVO.getPassword());
        // 保存12306抢票任务信息
        Ticket12306Order ticketTask = new Ticket12306Order();
        BeanUtil.copyProperties(shakedownVO, ticketTask);
        ticketTask.setUserId(user12306.getId());
        ticket12306OrderService.save(ticketTask);

        /*System.out.println(shakedownVO.toString());
        String trainDate = shakedownVO.getTrainDate();
        String fromStation = shakedownVO.getFromStation();
        String toStation = shakedownVO.getToStation();
        String trainNum = shakedownVO.getTrainNum();
//        trainNum = "D1882,D2962,D1853,D4822,D2948,G2904,D1870,D2972,D1872,D2834,D1876";
        String seats = shakedownVO.getSeatType();
        Shakedown12306Kit s12306 = new Shakedown12306Kit(shakedownVO.getUsername(), shakedownVO.getPassword());
        s12306.initQueryInfo(trainDate, fromStation, toStation, trainNum, seats);
        s12306.run();*/
        return R.ok();
    }
}
