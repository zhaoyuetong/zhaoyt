package com.atzyt.springsecurity01.service.impl;

import com.atzyt.springsecurity01.domain.LoginUser;
import com.atzyt.springsecurity01.domain.ResponseResult;
import com.atzyt.springsecurity01.domain.User;
import com.atzyt.springsecurity01.service.LoginService;
import com.atzyt.springsecurity01.utils.JwtUtil;
import com.atzyt.springsecurity01.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager的authenticate方法来进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //认证没通过给出提示
        if (Objects.isNull(authenticationToken)){
            new RuntimeException("登录失败");
        }
        //认证通过使用userid生产一个jwt 存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整的用户信息存入redis，userid作为key
        redisCache.setCacheObject("login:"+userid,loginUser);
        return new ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult logout() {
        //获取userid
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"注销成功");
    }
}
