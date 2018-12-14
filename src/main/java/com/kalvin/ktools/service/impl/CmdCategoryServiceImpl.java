package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.entity.CmdCategory;
import com.kalvin.ktools.dao.CmdCategoryDao;
import com.kalvin.ktools.service.CmdCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * linux命令分类 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2018-12-11
 */
@Service
public class CmdCategoryServiceImpl extends ServiceImpl<CmdCategoryDao, CmdCategory> implements CmdCategoryService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CmdCategoryServiceImpl.class);

    @Cacheable(value = Constant.CACHE_NAME, key = "\"cmdCategory\"")
    @Override
    public List<CmdCategory> getCmdCategories() {
        LOGGER.info("调用了缓存方法getCmdCategoryList");
        return list(null);
    }
}
