package com.kalvin.ktools.comm.kit;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.ktools.exception.KTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String get = HttpUtil.get("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
        JSONObject jsonObject = JSONUtil.parseObj(get);
        if ((int) jsonObject.get("code") == 1) {
            throw new KTException("无效的IP");
        }
        JSONObject d = (JSONObject) jsonObject.get("data");
        return "" + d.get("country") + d.get("area") + d.get("region") + d.get("city") + (d.get("county").equals("XX") ? "" : d.get("county")) + " " + d.get("isp");
    }

    /**
     * 获取物理地址 todo 该方法不行的
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

}
