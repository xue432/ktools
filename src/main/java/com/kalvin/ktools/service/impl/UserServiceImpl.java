package com.kalvin.ktools.service.impl;

import com.kalvin.ktools.entity.User;
import com.kalvin.ktools.dao.UserDao;
import com.kalvin.ktools.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}
