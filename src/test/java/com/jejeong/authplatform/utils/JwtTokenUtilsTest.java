package com.jejeong.authplatform.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenUtilsTest {

  JwtTokenUtils jwtTokenUtils = new JwtTokenUtils();
  String originKey = "secretKeySecretKeySecretKeySecretKey";

  @Test
  @DisplayName("토큰 생성 확인")
  void test1() {

    // given, when
    String encodingKey = Base64.getEncoder().encodeToString(originKey.getBytes());
    String username = "kiki";
    String token = createToken(encodingKey, username, 60000000L);

    // then
    assertThat(token).isNotNull();
    assertThat(token).isNotEmpty();
  }

  @Test
  @DisplayName("토큰 정보 추출 확인")
  void test2() {
    // given
    String encodingKey = Base64.getEncoder().encodeToString(originKey.getBytes());
    String username = "kiki";
    String token = createToken(encodingKey, username, 60000000L);

    // then
    assertThat(username).contains(jwtTokenUtils.getUsername(token, encodingKey));
  }

  @Test
  @DisplayName("토큰 유효성 확인")
  void test3() {
    // given
    String encodingKey = Base64.getEncoder().encodeToString(originKey.getBytes());
    String username = "kiki";
    String token = createToken(encodingKey, username, 60000000L);

    // when
    boolean isValid = jwtTokenUtils.validate(token, username, encodingKey);

    // then
    assertThat(isValid).isTrue();
  }

  @Test
  @DisplayName("인코딩 키가 맞는지 확인")
  void test4() {
    // given
    String encodingKey = Base64.getEncoder().encodeToString(originKey.getBytes());
    String username = "kiki";
    String token = createToken(encodingKey, username, 60000000L);

    // when
    boolean isValid = jwtTokenUtils.validate(token, "test", encodingKey);

    // then
    assertThat(isValid).isFalse();
  }

  @Test
  @DisplayName("토큰 만료 여부 확인")
  void test5() {

    // given
    String encodingKey = Base64.getEncoder().encodeToString(originKey.getBytes());
    String username = "kiki";
    String token = createToken(encodingKey, username, 600L);

    // when, then
    assertThatThrownBy(() -> jwtTokenUtils.validate(token, "test", encodingKey))
        .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
  }

  private String createToken(String encodingKey, String subject, long expireTime) {
    return jwtTokenUtils.doGenerateToken(subject, expireTime, encodingKey);
  }
}