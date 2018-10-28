package com.kalvin.ktools.comm.kit;

import cn.hutool.core.io.FileUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.exception.KTException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * IO工具类
 */
public class IoKit {

    /**
     * multipartFile转File
     * @param multipartFile m
     * @return file
     */
    public static File multipartFile2File(MultipartFile multipartFile) {
        File file;
        assert multipartFile != null;
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1);
        try {
            file = File.createTempFile(fileName, suffix);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new KTException("multipartFile转file发生异常");
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
