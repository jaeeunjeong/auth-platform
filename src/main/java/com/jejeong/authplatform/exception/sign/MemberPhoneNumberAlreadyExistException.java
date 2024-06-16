package com.jejeong.authplatform.exception.sign;

public class MemberPhoneNumberAlreadyExistException extends RuntimeException {

  public MemberPhoneNumberAlreadyExistException(String message) {
    super(message);
  }
}
