package com.kalvin.ktools.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.comm.kit.HttpServletContextKit;
import com.kalvin.ktools.comm.kit.IoKit;
import com.kalvin.ktools.comm.kit.KApiKit;
import com.kalvin.ktools.entity.R;
import com.kalvin.ktools.exception.KTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;

/**
 * 图片工具 控制层
 */
@RestController
@RequestMapping(value = "image")
public class ImageController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    @Value(value = "${kt.kapi.image.upload.url}")
    private String kApiImageUploadUrl;

    @Value(value = "${kt.kapi.image.handle.url}")
    private String kApiImageHandleUrl;

    @Value(value = "${kt.kapi.image.ascii.url}")
    private String kApiImageToAsciiUrl;

    @Value(value = "${kt.image.upload.dir}")
    private String imageUploadDir;

    @Value(value = "${kt.image.handle.dir}")
    private String imageHandleDir;

    @Value(value = "${kt.kapi.image.toGif.url}")
    private String kApiImageToGifUrl;

    @Value(value = "${kt.kapi.image.toColAscii.url}")
    private String kApiImageToColAsciiUrl;

    @Value(value = "${kt.kapi.gif.toAsciiGif.url}")
    private String kApiGifToAsciiGifUrl;

    @GetMapping(value = "artPic")
    public ModelAndView artPic() {
        return new ModelAndView("image/art_pic.html");
    }

    @GetMapping(value = "asciiPic")
    public ModelAndView asciiPic() {
        return new ModelAndView("image/ascii_pic.html");
    }

    @GetMapping(value = "gif")
    public ModelAndView gif() {
        return new ModelAndView("image/gif.html");
    }

    @GetMapping(value = "watermark")
    public ModelAndView watermark() {
        return new ModelAndView("image/ascii_pic.html");
    }

    @GetMapping(value = "asciiGif")
    public ModelAndView asciiGif() {
        return new ModelAndView("image/ascii_gif.html");
    }

    @GetMapping(value = "colorAsciiPic")
    public ModelAndView colorAsciiPic() {
        return new ModelAndView("image/ascii_color_pic.html");
    }

    @PostMapping(value = "upload")
    public R upload(@RequestParam(value = "file") MultipartFile file) {
        if (file == null) {
            throw new KTException("请先选择要上传的图片");
        }
        File wfile = IoKit.writeMultipartFile(file, imageUploadDir);
        return R.ok(wfile.getName());
    }

    /**
     * 图片处理
     * @param fileName 文件名
     * @param type  转化类型
     * @return r
     */
    @PostMapping(value = "handle")
    public R handle(String fileName, Integer type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", fileName);
        hashMap.put("image_type", type);
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        String post = HttpUtil.post(kApiImageHandleUrl, hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());   // 返回处理后图片相对路径
        }
        return r;
    }

    @PostMapping(value = "uploadAndHandle")
    public R uploadAndHandle(Integer type, @RequestParam(value = "file") MultipartFile file) {
        final R upload = this.upload(file);
        return this.handle(upload.getData().toString(), type);   // 转化图片
    }

    /**
     * 图片下载
     * @param fileName 图片文件名
     * @return r
     */
    @GetMapping(value = "download")
    public R download(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return R.fail("fileName参数不允许为空");
        }
        // 图片下载规则：首先判断classpath路径是否存在此图片，存在则从这里下载；否则从图片上传目录下载
        String fileUrl = Constant.CLASSPATH_HANDLE_IMAGE_DIR + fileName;
        File file = new File(fileUrl);
        if (!file.exists()) {
            fileUrl = imageHandleDir + fileName;
            file = new File(fileUrl);
        }
        ServletUtil.write(HttpServletContextKit.getHttpServletResponse(), file);
        return R.ok();
    }

    /**
     * 转化字符画
     * @param file file
     * @return r
     */
    @PostMapping(value = "to/ascii")
    public R toAscii(@RequestParam(value = "file") MultipartFile file) {
        R upload = this.upload(file);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", upload.getData());
        String post = HttpUtil.post(kApiImageToAsciiUrl, hashMap);
        return KApiKit.respone2R(post);
    }

    /**
     * 多张图片合成gif动图
     * @param imageArr 图片名称数组
     * @param duration 合成gif动图，每张图片时间间隔；单位（秒）
     * @return r
     */
    @PostMapping(value = "to/gif")
    public R toGif(String[] imageArr, Float duration) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("images", imageArr);
        hashMap.put("duration", duration);
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        String post = HttpUtil.post(kApiImageToGifUrl, hashMap);
        LOGGER.info("post={}", post);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            // 返回处理后缩略gif相对路径(带_sl.gif)
            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());
        }
        return r;
    }

    /**
     * gif转化成ascii字符gif动态图
     * @param file file
     * @param fileName 文件名，如果为空则需要先上传
     * @return r
     */
    @PostMapping(value = "gif/2AsciiGif")
    public R gif2AsciiGif(@RequestParam(value = "file") MultipartFile file, String fileName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (StrUtil.isEmpty(fileName)) {
            R upload = this.upload(file);
            hashMap.put("filename", upload.getData());
        } else {
            hashMap.put("filename", fileName);
        }
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        String post = HttpUtil.post(kApiGifToAsciiGifUrl, hashMap);
        LOGGER.info("post={}", post);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            // 返回处理后缩略gif相对路径(带_sl.gif)
            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());
        }
        return r;
    }

    /**
     * 图片转彩色ascii图片
     * @param file file
     * @param fileName 文件名，如果为空则需要先上传
     * @return r
     */
    @PostMapping(value = "to/colorAsciiPic")
    public R toColorAsciiPic(@RequestParam(value = "file") MultipartFile file, String fileName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (StrUtil.isEmpty(fileName)) {
            R upload = this.upload(file);
            hashMap.put("filename", upload.getData());
        } else {
            hashMap.put("filename", fileName);
        }
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        hashMap.put("useless", ""); // todo 无用的参数,为了让idea不显示重复代码（强迫症受不了）
        String post = HttpUtil.post(kApiImageToColAsciiUrl, hashMap);
        LOGGER.info("post={}", post);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            // 返回处理后缩略图相对路径(带_sl.png)
            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());
        }
        return r;
    }

    /**
     * todo 图片加水印功能
     * @param file file
     * @return r
     */
    @PostMapping(value = "add/watermark")
    public R addWatermark(@RequestParam(value = "file") MultipartFile file) {
        return R.ok();
    }

}
