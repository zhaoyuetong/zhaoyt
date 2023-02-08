package com.atzyt.springsecurity01.controller;

import com.atzyt.springsecurity01.domain.ResponseResult;
import com.atzyt.springsecurity01.domain.User;
import com.atzyt.springsecurity01.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //登录
        return loginService.login(user);
    }

    @RequestMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
