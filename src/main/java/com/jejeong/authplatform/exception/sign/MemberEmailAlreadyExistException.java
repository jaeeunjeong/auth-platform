package com.jejeong.authplatform.exception.sign;

public class MemberEmailAlreadyExistException extends RuntimeException {

  public MemberEmailAlreadyExistException(String message) {
    super(message);
  }
}
