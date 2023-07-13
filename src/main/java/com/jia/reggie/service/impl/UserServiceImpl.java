package com.jia.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.reggie.entity.User;
import com.jia.reggie.mapper.UserMapper;
import com.jia.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author kk
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
