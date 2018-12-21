package com.kalvin.ktools.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.dto.R;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理 控制层
 */
@RestController
@RequestMapping(value = "back")
public class BackController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BackController.class);

    @GetMapping(value = "push")
    public ModelAndView push() {
        return new ModelAndView("back/push.html");
    }

    /**
     * 链接提交到百度站长平台
     * @param links 链接，多个使用回车换行
     * @return
     */
    @PostMapping(value = "push/link")
    public R pushLink(@Param("links") String links) {
        String pushApi = "http://data.zz.baidu.com/urls?site=tools.kalvinbg.cn&token=itHiTMh1yyagufA9";
        HttpRequest post = HttpUtil.createPost(pushApi);
        post.contentType("text/plain");
        post.contentLength(83);
        links = links.replaceAll("\r", "").trim();
        post.body(links);
        HttpResponse response = post.execute();
        return R.ok(response.body());
    }
}
