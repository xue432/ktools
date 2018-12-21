package com.kalvin.ktools.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.kalvin.ktools.comm.constant.KApi;
import com.kalvin.ktools.comm.kit.KApiKit;
import com.kalvin.ktools.dto.R;
import com.kalvin.ktools.service.CmdCategoryService;
import com.kalvin.ktools.service.LinuxCmdService;
import com.kalvin.ktools.vo.StressTestingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 开发者工具 控制层
 */
@RestController
@RequestMapping(value = "dev")
public class DevController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DevController.class);

    @Autowired
    private CmdCategoryService cmdCategoryService;

    @Autowired
    private LinuxCmdService linuxCmdService;

    @Resource
    private KApi kApi;

    @GetMapping(value = "stress/testing")
    public ModelAndView index() {
        return new ModelAndView("dev/stress_testing.html");
    }

    @GetMapping(value = "json/format")
    public ModelAndView jsonFormat() {
        return new ModelAndView("dev/json_format.html");
    }

    @GetMapping(value = "linux/cmd")
    public ModelAndView linuxCmd() {
        ModelAndView mv = new ModelAndView("dev/linux_cmd.html");
        mv.addObject("cmdCategories", cmdCategoryService.getCmdCategories());
        return mv;
    }

    @GetMapping(value = "colorPicker")
    public ModelAndView colorPicker() {
        return new ModelAndView("dev/color_picker.html");
    }

  /**
     * 压力测试
     * @param stressTestingVO 压力测试参数
     * @return r
     */
    @PostMapping(value = "stressTesting")
    public R stressTesting(StressTestingVO stressTestingVO) {
        if (stressTestingVO.getConcurrent() > 300) { // 并发数最大限制300
            stressTestingVO.setConcurrent(300);
        }
        String post = HttpUtil.post(kApi.getDevStressTestingUrl(), BeanUtil.beanToMap(stressTestingVO));
        LOGGER.info("post={}", post);
        return KApiKit.respone2R(post);
    }

    /**
     * linux命令查询 todo
     * @param word 关键词
     * @param type 方式：0-普通的模糊查询 1-高级的模糊查询
     * @return
     */
    @GetMapping(value = "linuxCmd/query")
    public R linuxCmdQuery(String word, Integer type) {
        if (type == 0) {
            return R.ok(linuxCmdService.getByCmdOrName(word));
        } else if (type == 1) {
            return R.ok(linuxCmdService.getByKeyword(word));
        } else {
            return R.fail("参数异常");
        }
    }

    /**
     * linux命令查询,按分类
     * @param cmdCategoryId 命令分类ID
     * @return
     */
    @GetMapping(value = "linuxCmd/category")
    public R linuxCmdCategory(Long cmdCategoryId) {
        return R.ok(linuxCmdService.getByCmdCategoryId(cmdCategoryId));
    }

}
