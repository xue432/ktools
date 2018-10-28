package com.kalvin.ktools.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.comm.kit.HttpServletContextKit;
import com.kalvin.ktools.comm.kit.IoKit;
import com.kalvin.ktools.comm.kit.KApiKit;
import com.kalvin.ktools.entity.R;
import com.kalvin.ktools.exception.KTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.HashMap;

/**
 * 图片工具 控制层
 */
@RestController
@RequestMapping(value = "image")
public class ImageController {

    @Value(value = "${kt.kapi.image.upload.url}")
    private String pythonUploadUrl;

    @Value(value = "${kt.kapi.image.handle.url}")
    private String pythonHandleUrl;

    @PostMapping(value = "uploadAndHandle")
    public R uploadAndHandle(Integer type, MultipartHttpServletRequest request) {
        final MultipartFile multipartFile = request.getFile("image");
        if (multipartFile == null) {
            throw new KTException("请先选择要上传的图片");
        }

        File file = IoKit.multipartFile2File(multipartFile);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("image_type", type);
        hashMap.put("file", file);
        String post = HttpUtil.post(pythonUploadUrl, hashMap);

        IoKit.delFiles(file);

        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            return this.handle(r.getData().toString(), type);   // 转化图片
        } else {
            return r;
        }
    }

    @PostMapping(value = "handle")
    public R handle(String fileName, Integer type) {
        String handleDir = ClassUtil.getClassPath() + Constant.HANDLE_IMAGE_REF_DIR;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", fileName);
        hashMap.put("image_type", type);
        hashMap.put("handle_dir", handleDir);
        String post = HttpUtil.post(pythonHandleUrl, hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            fileName = r.getData().toString();
//            KApiKit.copyFromKapiImageByName(fileName);
            return R.ok(Constant.HANDLE_IMAGE_URL +
                    fileName.replace("upload", "handle"));   // 返回处理后图片相对路径
        }
        return r;
    }

    @GetMapping(value = "download")
    public R download(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return R.fail("fileName参数不允许为空");
        }
        final String fileUrl = ClassUtil.getClassPath() + Constant.HANDLE_IMAGE_REF_DIR + fileName;
        ServletUtil.write(HttpServletContextKit.getHttpServletResponse(), new File(fileUrl));
        return R.ok();
    }

}
