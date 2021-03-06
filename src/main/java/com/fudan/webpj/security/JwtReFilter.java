package com.fudan.webpj.security;

import com.alibaba.fastjson.JSONObject;
import com.fudan.webpj.entity.User;
import com.fudan.webpj.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtReFilter extends OncePerRequestFilter {
    @Value("${validate.url0}")
    private String validateUrl0;
    @Value("${validate.url1}")
    private String validateUrl1;
    @Value("${validate.url2}")
    private String validateUrl2;
    @Value("${token_Header}")
    private String tokenHeader;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Autowired
    public JwtReFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization: " + authorization);
        System.out.println(request.getRequestURI());
        if (request.getRequestURI().equals(validateUrl0)
                || request.getRequestURI().equals(validateUrl1)
                || request.getRequestURI().contains(validateUrl2)
        ) {
            //在登陆或者注册
            filterChain.doFilter(request, response);
        } else if (authorization != null
                && !authorization.isEmpty()
                && authorization.startsWith(tokenHeader)
        ) {
            String token = jwtTokenUtil.resolveToken(authorization);
            System.out.println("token:" + token);
            try {
                String id = jwtTokenUtil.getUserId(token);
                User user = userRepository.findByUserId(id);
                if (!jwtTokenUtil.validateToken(token) || user == null) {
                    System.out.println("超时或者用户不存在");
                    response401(response);
                } else {
                    System.out.println("正常");
                    filterChain.doFilter(request, response);
                }
            } catch (ExpiredJwtException ex) {
                System.out.println("401.1");
                response401(response);
            }
        } else {
            System.out.println("token为null或者token为空或者token的头部不以bearer开头");
            response401(response);
        }
    }

    private void response401(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        JSONObject data = new JSONObject();
        data.put("state", 401);
        writer.print(data);
        writer.flush();
        writer.close();
    }
}