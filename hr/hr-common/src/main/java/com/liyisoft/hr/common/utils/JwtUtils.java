package com.liyisoft.hr.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT 核心工具类
 */
@Component
public class JwtUtils {

  // 至少32个字符的密钥
  private final String SECRET = "LiyiSoftHumanResourceManagementSystemSecretKey20268888";
  private final long EXPIRATION = 7200000L; // 2 hours
  private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

  /**
   * 生成 Token
   * 
   * @param claims  载荷
   * @param subject 主题
   * @return Token 字符串
   */
  public String generateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(key)
        .compact();
  }

  /**
   * 从 Token 中获取主题
   * 
   * @param token Token 字符串
   * @return 主题
   */
  public String getSubjectFromToken(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}
