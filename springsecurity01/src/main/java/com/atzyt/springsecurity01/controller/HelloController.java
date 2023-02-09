package com.atzyt.springsecurity01.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Value("${menu.authority}")
    public String authority;
    @RequestMapping("hello")
    @PreAuthorize("hasAuthority('system:dept:list111')")
    //调用自定义授权方法
    //@PreAuthorize("@ex.hasAuth('system:dept:list')")
    public String hello(){
        System.out.println(authority);
        return "hello";
    }
}
