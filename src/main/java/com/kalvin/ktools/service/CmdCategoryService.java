package com.kalvin.ktools.service;

import com.kalvin.ktools.entity.CmdCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * linux命令分类 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2018-12-11
 */
public interface CmdCategoryService extends IService<CmdCategory> {

    List<CmdCategory> getCmdCategories();

}
