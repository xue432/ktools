package com.kalvin.ktools.comm.kit;

import com.kalvin.ktools.comm.ext.AnimatedGifEncoder;
import com.kalvin.ktools.comm.ext.GifDecoder;
import com.kalvin.ktools.exception.KTException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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

    /**
     * 缩放图片(压缩图片质量，改变图片尺寸)
     * 若原图宽度小于新宽度，则宽度不变！
     *
     * @param sourcePath 原图片路径地址
     * @param targetPath  压缩后输出路径地址
     * @param maxWidth     最大宽度
     * @param maxHeight    最大高度
     * @param quality      图片质量参数 0.7f 相当于70%质量
     * @param type     图片类型：IMG-普通图片；GIF-GIF图片
     * @param gifImg   gif图片对象
     */
    public static void resize(String sourcePath, String targetPath,
                                   Integer maxWidth, int maxHeight, float quality, String type, Image gifImg) {
        if (quality > 1) {
            throw new KTException("图片质量需设置在0.1-1范围");
        }

        try {
            File resizedFile = new File(targetPath);
            Image i;
            if ("IMG".equals(type)) {
                File originalFile = new File(sourcePath);
                ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
                i = ii.getImage();
            } else if ("GIF".equals(type)) {
                i = gifImg;
            } else {
                throw new KTException("图片类型不正确：type=" + type);
            }
            Image resizedImage = null;

            int iWidth = i.getWidth(null);
            int iHeight = i.getHeight(null);

            int newWidth;
            if (maxWidth == null || iWidth < maxWidth) {
                newWidth = iWidth;
            } else {
                newWidth = maxWidth;
            }

            if (iWidth >= iHeight) {
                resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
            }

            int newHeight = maxHeight;
            if (iHeight < maxHeight) {
                newHeight = iHeight;
            }

            if (resizedImage == null && iHeight >= iWidth) {
                resizedImage = i.getScaledInstance((newHeight * iWidth) / iHeight,
                        newHeight, Image.SCALE_SMOOTH);
            }

            // 此代码确保加载图像中的所有像素
            Image temp = new ImageIcon(resizedImage).getImage();

            // 创建缓冲图像
            BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
                    temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 将图像复制到缓冲图像
            Graphics g = bufferedImage.createGraphics();

            // 清除背景并绘制图像。
            g.setColor(Color.white);
            g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
            g.drawImage(temp, 0, 0, null);
            g.dispose();

            float softenFactor = 0.05f;
            float[] softenArray = {0, softenFactor, 0, softenFactor,
                    1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
            Kernel kernel = new Kernel(3, 3, softenArray);
            ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            bufferedImage = cOp.filter(bufferedImage, null);

            // 将jpeg写入文件
            FileOutputStream out = new FileOutputStream(resizedFile);

            // 将图像编码为jpeg数据流
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);

            param.setQuality(quality, true);

            encoder.setJPEGEncodeParam(param);
            encoder.encode(bufferedImage);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new KTException("图片压缩失败：" + e.getMessage());
        }
    }

    public static void compressPic(String sourcePath, String targetPath, Integer maxWidth, Float quality) {
        ImageKit.resize(sourcePath, targetPath, maxWidth, 50, quality, "IMG", null);
    }

    public static void compressGif(String sourcePath, String targetPath, int maxWidth, float quality) {
        GifDecoder decoder = new GifDecoder();
        int status = decoder.read(sourcePath);
        if (status != GifDecoder.STATUS_OK) {
            throw new KTException("read image error!");
        }
        // 保存的目标图片
        AnimatedGifEncoder e = new AnimatedGifEncoder();
        e.start(targetPath);
        e.setRepeat(decoder.getLoopCount());
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            BufferedImage image = decoder.getFrame(i);
            // 将每一贞图片压缩
            final String targetJpgPath = targetPath.replace(".gif", "_" + i + ".png").replace(".GIF", "_" + i + ".png");
            ImageKit.resize(null, targetJpgPath, maxWidth, 50, quality, "GIF", image);
            // 将压缩完的图片读取入BufferedImage缓冲对象
            Image temp = new ImageIcon(targetJpgPath).getImage();
            image = new BufferedImage(temp.getWidth(null),
                    temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
            e.setDelay(decoder.getDelay(0));
            e.setTransparent(Color.WHITE);
            e.addFrame(image);
        }
        e.finish();
    }

    public static void jpgToGif() {

    }
}
