package com.liyisoft.hr.admin;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * hr-admin 主启动类
 * scanBasePackages 覆盖整个 com.liyisoft.hr 顶层包，
 * 确保 hr-core、hr-common 等子模块中的 @Configuration、@Component 等 Bean 都能被扫描到。
 * 暂时排除数据库相关自动配置，待后续接入真实数据库时移除这些 exclude。
 */
@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class
    },
    scanBasePackages = {"com.liyisoft.hr"}
)
public class HrAdminApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(HrAdminApplication.class, args);
    }
}
