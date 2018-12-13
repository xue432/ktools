package com.kalvin.ktools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.ktools.dao.LinuxCmdDao;
import com.kalvin.ktools.entity.LinuxCmd;
import com.kalvin.ktools.service.LinuxCmdService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * linux命令表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2018-12-11
 */
@Service
public class LinuxCmdServiceImpl extends ServiceImpl<LinuxCmdDao, LinuxCmd> implements LinuxCmdService {

    @Override
    public List<LinuxCmd> getByCmdCategoryId(Long cmdCategoryId) {
        return list(new QueryWrapper<LinuxCmd>().eq("cmd_category_id", cmdCategoryId));
    }
}
