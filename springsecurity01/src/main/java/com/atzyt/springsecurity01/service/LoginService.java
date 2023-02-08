package com.atzyt.springsecurity01.service;

import com.atzyt.springsecurity01.domain.ResponseResult;
import com.atzyt.springsecurity01.domain.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
