package com.kalvin.ktools.controller;

import com.kalvin.ktools.comm.constant.KApi;
import com.kalvin.ktools.comm.kit.IoKit;
import com.kalvin.ktools.comm.kit.KApiKit;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.exception.KTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;

/**
 * 视频工具 控制层
 */
@RestController
@RequestMapping(value = "video")
public class VideoController {

    /*@Value(value = "${kt.kapi.video.toAsciiGif.url}")
    private String kApiVideoToAsciiGif;*/

    @Value(value = "${kt.video.upload.dir}")
    private String videoUploadDir;

    @Resource
    private KApi kApi;

    @Value(value = "${kt.kapi.token}")
    private String reqToken;

    @GetMapping(value = "")
    public ModelAndView index() {
        return new ModelAndView("");
    }

    @PostMapping(value = "upload")
    public R upload(MultipartHttpServletRequest request) {
        final MultipartFile multipartFile = request.getFile("video");
        if (multipartFile == null) {
            throw new KTException("请先选择要上传的视频");
        }
        File file = IoKit.writeMultipartFile(multipartFile, videoUploadDir, true);
        return R.ok(file.getName());
    }

    // 视频转字符动画gif
    @PostMapping(value = "to/asciiGif")
    public R toAsciiGif(MultipartHttpServletRequest request) {
        R upload = this.upload(request);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("video_name", upload.getData());

        String post = KApiKit.post(kApi.getVideo2AsciiGifUrl(), hashMap, reqToken);
        return KApiKit.respone2R(post);
    }
}
