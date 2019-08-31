package com.kalvin.ktools.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.annotation.SiteStats;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.comm.constant.KApi;
import com.kalvin.ktools.comm.kit.*;
import com.kalvin.ktools.dto.ImageDTO;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.exception.KTException;
import com.kalvin.ktools.vo.ImageWatermarkVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;

/**
 * 图片工具 控制层
 */
@RestController
@RequestMapping(value = "image")
public class ImageController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    @Resource
    private KApi kApi;

    @Value(value = "${kt.image.upload.dir}")
    private String imageUploadDir;

    @Value(value = "${kt.image.handle.dir}")
    private String imageHandleDir;

    @SiteStats
    @GetMapping(value = "artPic")
    public ModelAndView artPic() {
        return new ModelAndView("image/art_pic.html");
    }

    @SiteStats
    @GetMapping(value = "asciiPic")
    public ModelAndView asciiPic() {
        return new ModelAndView("image/ascii_pic.html");
    }

    @SiteStats
    @GetMapping(value = "gif")
    public ModelAndView gif() {
        return new ModelAndView("image/gif.html");
    }

    @SiteStats
    @GetMapping(value = "watermark")
    public ModelAndView watermark() {
        return new ModelAndView("image/watermark.html");
    }

    @SiteStats
    @GetMapping(value = "asciiGif")
    public ModelAndView asciiGif() {
        return new ModelAndView("image/ascii_gif.html");
    }

    @SiteStats
    @GetMapping(value = "colorAsciiPic")
    public ModelAndView colorAsciiPic() {
        return new ModelAndView("image/ascii_color_pic.html");
    }

    @SiteStats
    @GetMapping(value = "wordCloud")
    public ModelAndView wordCloud() {
        return new ModelAndView("image/wordcloud.html");
    }

    @SiteStats
    @GetMapping(value = "compress")
    public ModelAndView compress() {
        return new ModelAndView("image/compress");
    }

    /**
     * 异步上传
     * @param file f
     * @param type 类型。0：上传后不改变文件名称；1：上传后使用自定义文件名称
     * @return r
     */
    @SiteStats
    @PostMapping(value = "upload/{type}")
    public R upload(@RequestParam(value = "file") MultipartFile file, @PathVariable Integer type) {
        if (file == null) {
            throw new KTException("请先选择要上传的图片");
        }
        File f;
        switch (type) {
            case 0:
                f = IoKit.writeMultipartFile(file, imageUploadDir, false);
                break;
            case 1:
                f = IoKit.writeMultipartFile(file, imageUploadDir, true);
                break;
            default:
                throw new KTException("上传类型参数不正确");
        }
        return R.ok(f.getName());
    }

    /**
     * todo 同步上传
     * @param file file
     * @return r
     */
    @PostMapping(value = "uploadSync")
    public R uploadSync(@RequestParam(value = "file") MultipartFile[] file) {
        LOGGER.info("file={}", file.length);
        return R.ok();
    }

    /**
     * 艺术图片处理
     * @param fileName 文件名
     * @param type  转化类型
     * @return r
     */
    @SiteStats
    @PostMapping(value = "handle")
    public R handle(String fileName, Integer type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", fileName);
        hashMap.put("image_type", type);
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        String post = HttpUtil.post(kApi.getImgHandleUrl(), hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            final String fileUrl = imageHandleDir + r.getData().toString();
            return R.ok(new ImageDTO(r.getData().toString(), ImageKit.toBase64(new File(fileUrl))));
//            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());   // 返回处理后图片相对路径
        }
        return r;
    }

    @SiteStats
    @PostMapping(value = "uploadAndHandle")
    public R uploadAndHandle(Integer type, @RequestParam(value = "file") MultipartFile file) {
        final R upload = this.upload(file, 1);
        return this.handle(upload.getData().toString(), type);   // 转化图片
    }

    /**
     * 图片下载
     * @param fileName 图片文件名
     * @return r
     */
    @SiteStats
    @GetMapping(value = "download")
    public R download(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return R.fail("fileName参数不允许为空");
        }
        // 图片下载规则：首先判断classpath路径是否存在此图片，存在则从这里下载；否则从图片上传目录下载 todo
        String fileUrl = Constant.CLASSPATH_HANDLE_IMAGE_DIR + fileName;
        File file = new File(fileUrl);
        if (!file.exists()) {
            fileUrl = imageHandleDir + fileName;
            file = new File(fileUrl);
            if (!file.exists()) {
                throw new KTException("系统找不到此文件");
            }
        }
        ServletUtil.write(HttpServletContextKit.getHttpServletResponse(), file);
        return null;
    }

    /**
     * 转化字符画
     * @param file file
     * @return r
     */
    @SiteStats
    @PostMapping(value = "to/ascii")
    public R toAscii(@RequestParam(value = "file") MultipartFile file) {
        R upload = this.upload(file, 1);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", upload.getData());
        String post = HttpUtil.post(kApi.getImg2AsciiUrl(), hashMap);
        return KApiKit.respone2R(post);
    }

    /**
     * 多张图片合成gif动图
     * @param imageArr 图片名称数组
     * @param duration 合成gif动图，每张图片时间间隔；单位（秒）
     * @return r
     */
    @SiteStats
    @PostMapping(value = "to/gif")
    public R toGif(String[] imageArr, Float duration) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("images", imageArr);
        hashMap.put("duration", duration);
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        String post = HttpUtil.post(kApi.getImg2GifUrl(), hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            // 返回处理后缩略gif相对路径(带_sl.gif)
//            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());
            final String fileUrl = imageHandleDir + r.getData().toString();
            return R.ok(new ImageDTO(r.getData().toString(), ImageKit.toBase64(new File(fileUrl))));
        }
        return r;
    }

    /**
     * gif转化成ascii字符gif动态图
     * @param fileName 文件名，如果为空则需要先上传
     * @return r
     */
    @SiteStats
    @PostMapping(value = "gif/2AsciiGif")
    public R gif2AsciiGif(String fileName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", fileName);
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        String post = HttpUtil.post(kApi.getGif2AsciiUrl(), hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            // 返回处理后缩略gif相对路径(带_sl.gif)
//            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());
            final String fileUrl = imageHandleDir + r.getData().toString();
            return R.ok(new ImageDTO(r.getData().toString(), ImageKit.toBase64(new File(fileUrl))));
        }
        return r;
    }

    /**
     * 图片转彩色ascii图片
     * @param fileName 文件名
     * @return r
     */
    @SiteStats
    @PostMapping(value = "to/colorAsciiPic")
    public R toColorAsciiPic(String fileName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", fileName);
        hashMap.put("handle_dir", Constant.CLASSPATH_HANDLE_IMAGE_DIR);
        hashMap.put("useless", ""); // todo 无用的参数,为了让idea不显示重复代码（强迫症受不了）
        String post = HttpUtil.post(kApi.getImg2ColorAsciiUrl(), hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            // 返回处理后缩略图相对路径(带_sl.png)
//            return R.ok(Constant.HANDLE_IMAGE_URL + r.getData().toString());
            final String fileUrl = imageHandleDir + r.getData().toString();
            return R.ok(new ImageDTO(r.getData().toString(), ImageKit.toBase64(new File(fileUrl))));
        }
        return r;
    }

    /**
     * 图片加水印功能
     * @param imageWatermarkVO 水印参数实体
     * @return r
     */
    @SiteStats
    @PostMapping(value = "add/watermark")
    @ResponseBody
    public R addWatermark(@RequestBody ImageWatermarkVO imageWatermarkVO) {
        String handleName = KToolkit.imageAddWaterMark(
                imageUploadDir + imageWatermarkVO.getFileName(),
                imageWatermarkVO.getWaterMarkContent(),
                imageWatermarkVO.getRgb());
        String fileUrl = imageHandleDir + handleName;
        return R.ok(new ImageDTO(handleName, ImageKit.toBase64(new File(fileUrl))));
    }

    /**
     * 生成词云图
     * @param filename 文件名
     * @param wordContent 词云内容
     * @return r
     */
    @SiteStats
    @PostMapping(value = "generateWordCloud")
    public R generateWordCloud(String filename, String wordContent) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("filename", filename);
        hashMap.put("content", wordContent);
        String post = HttpUtil.post(kApi.getImgGenerateWordCloudUrl(), hashMap);
        R r = KApiKit.respone2R(post);
        if (KApiKit.isSuccess(post)) {
            final String fileUrl = imageHandleDir + r.getData().toString();
            return R.ok(new ImageDTO(r.getData().toString(), ImageKit.toBase64(new File(fileUrl))));
        }
        return r;
    }

    /**
     * 压缩图片
     * @param filename 图片名称(xx.jpg)
     * @param width 图片新宽度，若比原图宽度还大则使用原图宽度
     * @param quality 图片质量参数 0.8f 相当于80%质量
     * @return r
     */
    @SiteStats
    @PostMapping(value = "compress")
    public R compress(String filename, Integer width, Float quality) {
        final String handleFileUrl = imageHandleDir + filename;
        ImageKit.compressPic(imageUploadDir + filename, handleFileUrl, width, quality);
        File file = new File(handleFileUrl);
        ImageDTO imageDTO = new ImageDTO(filename, ImageKit.toBase64(file));
        imageDTO.setSize(file.length());
        return R.ok(imageDTO);
    }

}
