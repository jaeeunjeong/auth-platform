package com.jejeong.authplatform.service.sign;

import com.jejeong.authplatform.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

  private final JwtTokenUtils jwtTokenUtils;

  @Value("${jwt.token.expired-time-ms.access}")
  private long accessTokenExpiredTime;

  @Value("${jwt.token.expired-time-ms.refresh}")
  private long refreshTokenExpiredTime;

  @Value("${jwt.token.secret-key.access}")
  private String accessKey;

  @Value("${jwt.token.secret-key.refresh}")
  private String refreshKey;


  public String generateAccessToken(String username) {
    return jwtTokenUtils.doGenerateToken(username, accessTokenExpiredTime, accessKey);
  }

  public String generateRefreshToken(String username) {
    return jwtTokenUtils.doGenerateToken(username, refreshTokenExpiredTime, refreshKey);
  }

  public boolean validateAccessToken(String token, String username) {
    return jwtTokenUtils.validate(token, username, accessKey);
  }

  public boolean validateRefreshToken(String token, String username) {
    return jwtTokenUtils.validate(token, username, refreshKey);
  }

  public String extractAccessTokenSubject(String token) {
    return jwtTokenUtils.extractAllClaims(accessKey, token).getSubject();
  }

  public String extractRefreshTokenSubject(String token) {
    return jwtTokenUtils.extractAllClaims(refreshKey, token).getSubject();
  }

  public String extractAccessTokenUsername(String token) {
    return jwtTokenUtils.getUsername(token, accessKey);
  }

  public String extractRefreshTokenUsername(String token) {
    return jwtTokenUtils.getUsername(token, refreshKey);
  }
}
