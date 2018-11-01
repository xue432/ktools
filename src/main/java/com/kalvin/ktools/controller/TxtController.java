package com.kalvin.ktools.controller;

import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.kit.KApiKit;
import com.kalvin.ktools.entity.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * 文字工具 控制层
 */
@RestController
@RequestMapping(value = "txt")
public class TxtController {

    @Value(value = "${kt.kapi.txt.ascii.url}")
    private String kApiTxtAsciiUrl;

    @GetMapping(value = "ascii")
    public ModelAndView ascii() {
        return new ModelAndView("txt/ascii_txt.html");
    }

    @PostMapping(value = "to/ascii")
    public R toAscii(String txt, String font) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("txt", txt);
        hashMap.put("font", font);
        String post = HttpUtil.post(kApiTxtAsciiUrl, hashMap);

        return KApiKit.respone2R(post);
    }

}
