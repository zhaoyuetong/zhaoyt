package com.atzyt.service;

import com.atzyt.spring.MyApplicationContext;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        MyApplicationContext myApplicationContext = new MyApplicationContext(AppConfig.class);
        UserService userService = (UserService) myApplicationContext.getBean("userService");
//        System.out.println((UserService) myApplicationContext.getBean("userService"));
//        System.out.println((UserService) myApplicationContext.getBean("userService"));
//        System.out.println((UserService) myApplicationContext.getBean("userService"));
//        System.out.println((UserService) myApplicationContext.getBean("userService"));

    }
}
