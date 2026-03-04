package com.liyisoft.hr.core.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务实现
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 当前暂时直接提供一组默认验证，待实际对接 Mapper 时替换为 selectOne(SysUser) 查询
        if ("admin".equals(username)) {
            // "123456" 对应的 BCrypt 解密密文
            return new User("admin", "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2", Collections.emptyList());
        }
        throw new UsernameNotFoundException("用户不存在");
    }
}
