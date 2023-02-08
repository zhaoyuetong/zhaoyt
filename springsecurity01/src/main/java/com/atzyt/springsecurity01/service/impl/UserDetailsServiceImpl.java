package com.atzyt.springsecurity01.service.impl;

import com.atzyt.springsecurity01.domain.LoginUser;
import com.atzyt.springsecurity01.domain.User;
import com.atzyt.springsecurity01.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder bcpe;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> wrapper = queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)){
            new RuntimeException("用户名或者密码错误");
        }
        //TODO 查权限信息

        return new LoginUser(user);
    }
}
