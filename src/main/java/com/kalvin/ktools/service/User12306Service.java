package com.kalvin.ktools.service;

import com.kalvin.ktools.entity.User12306;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 12306账号信息表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-26
 */
public interface User12306Service extends IService<User12306> {

    User12306 saveUser12306(String username, String password);

}
