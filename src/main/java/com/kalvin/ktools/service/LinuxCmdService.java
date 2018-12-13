package com.kalvin.ktools.service;

import com.kalvin.ktools.entity.LinuxCmd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * linux命令表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2018-12-11
 */
public interface LinuxCmdService extends IService<LinuxCmd> {

    /**
     * 根据分类ID获取linux命令列表
     * @param cmdCategoryId 命令分类ID
     * @return
     */
    List<LinuxCmd> getByCmdCategoryId(Long cmdCategoryId);

}
