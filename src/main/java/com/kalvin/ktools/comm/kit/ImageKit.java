package com.kalvin.ktools.comm.kit;

import com.kalvin.ktools.exception.KTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Base64;

/**
 * 图片工具类
 */
public class ImageKit {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageKit.class);

    public static String toBase64(File file) {
        try {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            final InputStream is = new FileInputStream(file);
            final BufferedInputStream bis = new BufferedInputStream(is);
            byte[] bytes = new byte[bis.available()];
            int read = bis.read(bytes);
            byte[] encode = Base64.getEncoder().encode(bytes);
            String base64 = new String(encode);
            base64 = "data:image/" + suffix + ";base64," + base64;
            return base64;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new KTException("图片base64编码失败");
        }
    }
}
