package com.kalvin.ktools.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.comm.kit.KToolkit;
import com.kalvin.ktools.dao.LinuxCmdDao;
import com.kalvin.ktools.entity.LinuxCmd;
import com.kalvin.ktools.service.LinuxCmdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(LinuxCmdServiceImpl.class);

    @Cacheable(value = Constant.CACHE_NAME)
    @Override
    public List<LinuxCmd> getByCmdCategoryId(Long cmdCategoryId) {
        LOGGER.info("调用了缓存方法getByCmdCategoryId");
        return list(new QueryWrapper<LinuxCmd>().lambda().eq(LinuxCmd::getCmdCategoryId, cmdCategoryId));
    }

    @Cacheable(value = Constant.CACHE_NAME)
    @Override
    public List<LinuxCmd> getByKeyword(String keyword) {
        LOGGER.info("调用了缓存方法getByKeyword");
        List<LinuxCmd> list = this.getByCmdOrName(keyword);
        // 处理关键词
        keyword = keyword.replace(" ", "");
        keyword = keyword.replace("linux", "");
        keyword = keyword.replace("Linux", "");
        keyword = keyword.replace("LINUX", "");
        keyword = KToolkit.replacePunctuation(keyword);

        if (CollectionUtil.isEmpty(list)) {
            list = this.getByCmdOrName(keyword);
        }

        if (CollectionUtil.isEmpty(list)) {
            if (keyword.length() > 6) {
                Map<String, Object> map = this.splitKeyword(keyword);
                list = baseMapper.selectByKeywords(map);
            }
        }
        return list;
    }

    @Override
    public List<LinuxCmd> getByCmdOrName(String con) {
        if (StrUtil.isEmpty(con)) {
            return new ArrayList<>();
        }
        return list(new QueryWrapper<LinuxCmd>().lambda().like(LinuxCmd::getCmd, con).or().like(LinuxCmd::getName, con));
    }

    /**
     * 拆分关键词
     * @return list
     */
    private Map<String, Object> splitKeyword(String keyword) {
        List<String> list = new ArrayList<>();
        List<String> words = new ArrayList<>();
        List<String> letters = new ArrayList<>();
        StringBuilder letter = new StringBuilder();

        // 拆分成单个单词
        for (final String word : keyword.split("")) {
            if (Pattern.matches("[a-zA-Z]", word)) {
                letter.append(word);
            } else {
                words.add(word);
                if (letter.length() > 0) {
                    letters.add(letter.toString());
                    letter = new StringBuilder();
                }
            }
        }

        /*组装新的关键词组*/
        StringBuilder wg = new StringBuilder();
        final int wgLen = 3;    // 重组词组长度
        int wLen = words.size();
        boolean isEDiv = wLen % wgLen != 0;  // 是否整除
        int wgg = wLen / wgLen; // 总词组数


        // 从keyword开头开始拆分词组
        for (final String word : words) {
            wg.append(word);
            if (isEDiv) {
                if (wg.length() == wgLen) {
                    list.add(wg.toString());
                    wg = new StringBuilder();
                    wgg--;
                }
            } else {
                if (wg.length() == wgLen && wgg > 1) {
                    list.add(wg.toString());
                    wg = new StringBuilder();
                    wgg--;
                }
            }
        }
        if (wg.length() > 0) {
            list.add(wg.toString());
        }

        // 从keyword尾开始拆分词组
        if (!isEDiv) {
            wg = new StringBuilder();
            wgg = wLen / wgLen; // 总词组数
            for (int i = wLen - 1; i >= 0; i--) {
                wg.insert(0, words.get(i));
                if (wg.length() == wgLen && wgg > 1) {
                    list.add(wg.toString());
                    wg = new StringBuilder();
                    wgg--;
                }

            }
            if (wg.length() > 0) {
                list.add(wg.toString());
            }
        }

        list.addAll(letters);
//        list.forEach(System.out::println);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("letters", letters);
        hashMap.put("keywords", list);
        return hashMap;
    }

}
