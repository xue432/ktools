package com.kalvin.ktools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kalvin.ktools.entity.LinuxCmd;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * linux命令表 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2018-12-11
 */
public interface LinuxCmdDao extends BaseMapper<LinuxCmd> {

    /**
     * 根据关键词组查询
     * @param keywordMaps map{letters:list,keywords:list}
     * @return
     */
    List<LinuxCmd> selectByKeywords(Map keywordMaps);

}
