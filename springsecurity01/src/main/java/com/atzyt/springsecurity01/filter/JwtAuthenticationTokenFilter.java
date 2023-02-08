package com.atzyt.springsecurity01.filter;

import com.atzyt.springsecurity01.domain.LoginUser;
import com.atzyt.springsecurity01.utils.JwtUtil;
import com.atzyt.springsecurity01.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            //放行过滤器
            filterChain.doFilter(request,response);
            return;
        }
        //解析token获取其中的userid
        String userid = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //通过userid从redis中获取用户信息
        String redisKey = "";
        redisKey = "login:"+userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //TODO 把用户信息存入到SecurityContextHolder（获取权限信息封装到authentication中）为了让后面的filter知道这个请求是已认证的
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行过滤器
        filterChain.doFilter(request,response);
    }
}
