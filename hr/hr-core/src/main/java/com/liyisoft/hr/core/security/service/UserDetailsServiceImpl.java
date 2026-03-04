package com.liyisoft.hr.core.security.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务实现（临时硬编码，后续对接数据库替换）
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 启动时由 Spring 的 PasswordEncoder 动态生成哈希，避免手写哈希值出错
    private String adminPasswordHash;

    @PostConstruct
    public void init() {
        // admin 账号的测试密码为 "123456"
        this.adminPasswordHash = passwordEncoder.encode("123456");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 待后续真正对接数据库时，将此处替换为 Mapper 查询
        if ("admin".equals(username)) {
            return new User("admin", adminPasswordHash, Collections.emptyList());
        }
        throw new UsernameNotFoundException("用户不存在");
    }
}
