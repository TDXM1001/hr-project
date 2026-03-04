package com.liyisoft.hr.core.controller;

import com.liyisoft.hr.common.core.domain.Result;
import com.liyisoft.hr.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证接口控制器
 */
@RestController
@RequestMapping("/api/auth")
public class SysAuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> loginBody) {
        String username = loginBody.get("username");
        String password = loginBody.get("password");

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            return Result.error("用户不存在或密码错误");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            String token = jwtUtils.generateToken(claims, username);
            Map<String, String> res = new HashMap<>();
            res.put("token", token);
            return Result.success(res);
        } else {
            return Result.error("用户不存在或密码错误");
        }
    }
}
