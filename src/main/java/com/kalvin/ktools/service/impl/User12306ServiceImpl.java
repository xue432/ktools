package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.entity.User12306;
import com.kalvin.ktools.dao.User12306Dao;
import com.kalvin.ktools.exception.KTException;
import com.kalvin.ktools.service.User12306Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 12306账号信息表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-26
 */
@Service
public class User12306ServiceImpl extends ServiceImpl<User12306Dao, User12306> implements User12306Service {

    @Override
    public User12306 saveUser12306(String username, String password) {
        try {
            final User12306 user12306 = new User12306(username, password);
            super.save(user12306);
            return user12306;
        } catch (Exception e) {
            throw new KTException("保存12306账号时发生了异常");
        }
    }
}
