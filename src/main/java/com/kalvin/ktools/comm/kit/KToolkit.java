package com.kalvin.ktools.comm.kit;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.entity.RGB;
import com.kalvin.ktools.exception.KTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 工具集合类
 */
public class KToolkit {

    private final static Logger LOGGER = LoggerFactory.getLogger(KToolkit.class);

    /**
     * 获取IP信息
     *
     * @param ip IP地址
     * @return address
     */
    public static String getIPInfo(String ip) {
        final String taobaoIPApi = "http://ip.taobao.com/service/getIpInfo.php?ip=";
        try {
            String get = HttpUtil.get(taobaoIPApi + ip, 1000);
            JSONObject jsonObject = JSONUtil.parseObj(get);
            if ((int) jsonObject.get("code") == 1) {
                LOGGER.error("获取IP信息出错:{}", jsonObject.get("data"));
                return null;
            }
            JSONObject d = (JSONObject) jsonObject.get("data");
            return "" + d.get("country") + d.get("area") + d.get("region") + d.get("city") + (d.get("county").equals("XX") ? "" : d.get("county")) + " " + d.get("isp");
        } catch (Exception e) {
            LOGGER.info("获取IP信息时发生异常:{}", e.getMessage());
            return null;
        }

    }

    /**
     * 获取物理地址 todo 该方法不行的
     *
     * @param ip IP地址
     * @return
     */
    public static String getMacAddress(String ip) {
        try {
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(ip));
            byte[] mac = ne.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                System.out.println("每8位:" + str);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new KTException("获取Mac地址出错");
        }
    }

    /**
     * 图片添加水印
     * @param srcImgPath 图片url路径
     * @param waterMarkContent 水印文字
     * @return 处理后图片名称
     */
    public static String imageAddWaterMark(String srcImgPath, String waterMarkContent, RGB rgb) {
        try {
            String fileName = srcImgPath.substring(srcImgPath.lastIndexOf("/") + 1);
            fileName = fileName.replace(Constant.UPLOAD_PREFIX_FILENAME, Constant.HANDLE_PREFIX_FILENAME);
            String prefixPath = srcImgPath.substring(0, srcImgPath.lastIndexOf("/") + 1);
            String handlePath = prefixPath.replace(Constant.UPLOAD_PREFIX_FILENAME, Constant.HANDLE_PREFIX_FILENAME);
            if (StrUtil.isEmpty(prefixPath) || StrUtil.isEmpty(handlePath)) {
                throw new KTException("图片url路径不正确");
            }
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);// 得到文件
            Image srcImg = ImageIO.read(srcImgFile);// 文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);// 获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);// 获取图片的高
            Color markContentColor;
            if (rgb == null) {
                markContentColor = new Color(0, 0, 0, 128);
            } else {
                markContentColor = new Color(rgb.getR(), rgb.getG(), rgb.getB(), 128);
            }
//            LOGGER.info("size={}", 45*srcImgWidth/2560);
            int size = 0, df = 0;
            if (100 < srcImgHeight && srcImgWidth <= 500) {
                size = 15;
                df = 5;
            } else if (500 < srcImgHeight && srcImgWidth <= 1000) {
                size = 21;
                df = 9;
            } else if (1000 < srcImgWidth && srcImgWidth <= 1500) {
                size = 26;
                df = 12;
            } else if (1500 < srcImgWidth && srcImgWidth <= 2000) {
                size = 32;
                df = 18;
            } else if (2000 < srcImgWidth && srcImgWidth <= 2500) {
                size = 42;
                df = 22;
            } else if (2500 < srcImgWidth && srcImgWidth <= 3000) {
                size = 47;
                df = 26;
            }
            Font font = new Font("微软雅黑", Font.PLAIN, size);   // 45 25    size=27w/1280
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor); // 根据图片的背景设置水印颜色
            g.setFont(font);              // 设置字体

            // 设置水印的坐标
            int wh = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
            int x = srcImgWidth - wh;
            int y = srcImgHeight - 2 * wh;
            g.drawString(waterMarkContent, x, srcImgHeight - df);  // 画出水印 24
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(handlePath + fileName);
            ImageIO.write(bufImg, KToolkit.getFileSuffix(fileName), outImgStream);
            LOGGER.info("添加水印完成");
            outImgStream.flush();
            outImgStream.close();
            return fileName;
        } catch (Exception e) {
            throw new KTException("图片添加水印发现异常：" + e.getMessage());
        }
    }

    public static String getFileSuffix(String fileName) {
        if (StrUtil.isEmpty(fileName) || !fileName.contains(".")) {
            throw new KTException("参数错误");
        }
        // 返回文件后缀名，不带“.”
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String getFileNameWithoutSuffix(String fileName) {
        if (StrUtil.isEmpty(fileName) || !fileName.contains(".")) {
            throw new KTException("参数错误");
        }
        // 返回不带后缀的文件名
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 替换掉所有标点符号
     * @param str
     * @return 替换后的字符串
     */
    public static String replacePunctuation(String str) {
        return str.replaceAll("[\\pP]", "");
    }

}
