package com.liyisoft.hr.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 此时暂时排除自动配置数据源，待后续连通真实数据库再开启
 * hr-admin 主启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HrAdminApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(HrAdminApplication.class, args);
    }
}
