package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.User;
import com.maynormoe.takeout.mapper.UserMapper;
import com.maynormoe.takeout.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
