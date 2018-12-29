package com.kalvin.ktools.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.RandomUtil;
import com.kalvin.ktools.comm.annotation.SiteStats;
import com.kalvin.ktools.comm.kit.KToolkit;
import com.kalvin.ktools.dto.R;
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

    /**
     * 生成二维码
     * @param context 二维码内容
     * @return r
     */
    @SiteStats
    @GetMapping(value = "genQrCode")
    public R genQrCode(String context) {
        return R.ok();
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
     * @param ip ip地址
     * @return r
     */
    @SiteStats
    @GetMapping(value = "get/ipInfo")
    public R getIpInfo(String ip) {
        return R.ok(KToolkit.getIPInfo(ip));
    }
}
