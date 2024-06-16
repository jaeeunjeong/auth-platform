package com.jejeong.authplatform.exception.sign;

public class MemberNicknameAlreadyExistException extends RuntimeException {

  public MemberNicknameAlreadyExistException(String message) {
    super(message);
  }
}
