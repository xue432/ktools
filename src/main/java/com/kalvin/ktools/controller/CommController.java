package com.kalvin.ktools.controller;

import cn.hutool.core.util.ImageUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.kalvin.ktools.comm.kit.HttpServletContextKit;
import com.kalvin.ktools.comm.kit.IoKit;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.exception.KTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 通用 控制层
 */
@RestController
@RequestMapping(value = "comm")
public class CommController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommController.class);

    @Value(value = "${kt.image.handle.dir}")
    private String imageHandleDir;

    @GetMapping(value = "image/{fileName}")
    public ModelAndView image(@PathVariable String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileUrl = imageHandleDir + fileName;
        try {
            BufferedImage read = ImageIO.read(new File(fileUrl));
            String base64 = ImageUtil.toBase64(read, suffix);
            base64 = "data:image/" + suffix + ";base64," + base64;
            return new ModelAndView("comm/image.html").addObject("base64", base64);
        } catch (IOException e) {
            throw new KTException("sfksdjlfjl");
        }

    }

    /**
     * 访问静态图片
     *
     * @param fileName 文件名
     */
    @GetMapping(value = "static/image/{fileName}")
    public void staticImage(@PathVariable String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileUrl = imageHandleDir + fileName;
        try {
            HttpServletResponse response = HttpServletContextKit.getHttpServletResponse();
            ServletUtil.write(response,
                    new FileInputStream(new File(fileUrl)), "image/" + suffix);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
