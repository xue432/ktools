package com.kalvin.ktools.comm.kit;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.exception.KTException;

import java.util.Map;

/**
 * 处理KApi接口工具类
 */
public class KApiKit {

    public static boolean isSuccess(String respone) {
        JSONObject jsonObject = JSONUtil.parseObj(respone);
        Integer code = (Integer) jsonObject.get("code");
        return Constant.OK_CODE == code;
    }

    public static R respone2R(String respone) {
        JSONObject jsonObject;
        try {
            jsonObject = JSONUtil.parseObj(respone);
        } catch (Exception e) {
            throw new KTException(e.getMessage());
        }
        String msg = (String) jsonObject.get("msg");
        Object data = jsonObject.get("data");

        if (!KApiKit.isSuccess(respone)) {
            return R.fail(msg);
        }
        if (StrUtil.isNotEmpty(msg) && data != null) {
            return R.ok(msg, data);
        }  else if (data != null) {
            return R.ok(data);
        }
        return R.ok();
    }

    public static void copyFromKapiImageByName(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            throw new KTException("文件名称不允许为空");
        }
        fileName = fileName.replace("upload", "handle");
        System.out.println("handle_dir=" + Constant.HANDLE_IMAGE_REF_DIR + fileName);
        try {
            FileUtil.copy(Constant.KAPI_HANDLE_IMAGE_DIR + fileName,
                    Constant.HANDLE_IMAGE_REF_DIR + fileName, true);
        } catch (IORuntimeException e) {
            e.printStackTrace();
            throw new KTException("复制文件发生异常");
        }
    }

    public static String post(String url, Map<String, Object> formMap, String reqToken) {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.header("token", reqToken);
        return httpRequest.form(formMap).execute().body();
    }
}
