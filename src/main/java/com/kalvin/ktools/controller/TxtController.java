package com.kalvin.ktools.controller;

import com.kalvin.ktools.comm.annotation.SiteStats;
import com.kalvin.ktools.comm.constant.KApi;
import com.kalvin.ktools.comm.kit.KApiKit;
import com.kalvin.ktools.dto.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 文字工具 控制层
 */
@RestController
@RequestMapping(value = "txt")
public class TxtController {

    @Resource
    private KApi kApi;

    @Value(value = "${kt.kapi.token}")
    private String reqToken;

    @SiteStats
    @GetMapping(value = "ascii")
    public ModelAndView ascii() {
        return new ModelAndView("txt/ascii_txt");
    }

    @SiteStats
    @GetMapping(value = "charStatistics")
    public ModelAndView charStatistics() {
        return new ModelAndView("txt/char_statistics");
    }

    @SiteStats
    @PostMapping(value = "to/ascii")
    public R toAscii(String txt, String font) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("txt", txt);
        hashMap.put("font", font);
        String post = KApiKit.post(kApi.getTxt2AsciiUrl(), hashMap, reqToken);
        return KApiKit.respone2R(post);
    }

}
