package com.jejeong.authplatform.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {

  private String accessToken;
  private String refreshToken;
}
