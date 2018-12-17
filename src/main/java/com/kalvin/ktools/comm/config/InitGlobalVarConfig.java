package com.kalvin.ktools.comm.config;

import com.kalvin.ktools.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *  全局静态变量配置
 *  项目启动时执行
 */
@Order(2)
@Component
public class InitGlobalVarConfig implements ApplicationRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(InitGlobalVarConfig.class);

    @Resource
    private MenuService menuService;
    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("ktools 项目启动成功！");
        Map<String, Object> vars = new HashMap<>();
        vars.put("allMenu", menuService.getAllMenuHierarchy());
        thymeleafViewResolver.setStaticVariables(vars);
    }
}
