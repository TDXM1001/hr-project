package com.liyisoft.hr.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器独立配置类
 * 单独抽离避免与 SecurityConfig 形成循环依赖：
 * SecurityConfig → JwtFilter → UserDetailsServiceImpl → PasswordEncoder → SecurityConfig
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 注册 BCrypt 密码编码器 Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
