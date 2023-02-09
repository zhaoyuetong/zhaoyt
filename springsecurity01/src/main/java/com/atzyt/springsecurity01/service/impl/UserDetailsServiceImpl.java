package com.atzyt.springsecurity01.service.impl;

import com.atzyt.springsecurity01.domain.LoginUser;
import com.atzyt.springsecurity01.domain.User;
import com.atzyt.springsecurity01.mapper.MenuMapper;
import com.atzyt.springsecurity01.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> wrapper = queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)){
            new RuntimeException("用户名或者密码错误");
        }
        //查权限信息
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        //ArrayList<String> list = new ArrayList<>(Arrays.asList("test","admin"));
        return new LoginUser(user,list);
    }
}
