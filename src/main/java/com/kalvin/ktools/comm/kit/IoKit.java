package com.kalvin.ktools.comm.kit;

import cn.hutool.core.date.DateUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.exception.KTException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * IO工具类
 */
public class IoKit {

    /**
     * multipartFile写入磁盘文件
     * @param multipartFile m
     * @param dest 目标目录
     * @return file
     */
    public static File writeMultipartFile(MultipartFile multipartFile, String dest) {
        File file;
        assert multipartFile != null;
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = Constant.UPLOAD_PREFIX_FILENAME + DateUtil.format(new Date(), "yyMMddhhmmss") + suffix;
        try {
            file = new File(dest + fileName);
            file.createNewFile();
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new KTException("写入文件时发生异常");
        }
        return file;
    }

    public static void delFiles(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
