package com.kalvin.ktools.controller;

import com.kalvin.ktools.comm.kit.IoKit;
import com.kalvin.ktools.exception.KTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 通用 控制层
 */
@RestController
@RequestMapping(value = "comm")
public class CommController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommController.class);

    /**
     * 文件上传
     * @param file MultipartFIle
     * @param uploadDir 上传目录
     * @return
     */
    @PostMapping(value = "upload")
    public String upload(@RequestParam(value = "file") MultipartFile file, String uploadDir) {
        if (file == null) {
            throw new KTException("上传文件失败");
        }
        LOGGER.info("uploading file...", file.getOriginalFilename());
        File wFile = IoKit.writeMultipartFile(file, uploadDir);
        return "redirect:/image/to/gif?fileName=";
//        return R.ok(wFile.getName());
    }
}
