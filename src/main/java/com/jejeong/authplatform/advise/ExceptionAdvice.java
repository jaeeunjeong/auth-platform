package com.jejeong.authplatform.advise;

import com.jejeong.authplatform.dto.response.Response;
import com.jejeong.authplatform.exception.security.AccessDeniedException;
import com.jejeong.authplatform.exception.security.AuthenticationEntryPointException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response exception(Exception e) {
    log.info("e={}", e.getMessage());
    return Response.failure(-1000, "오류가 발생했습니다.");
  }

  @ExceptionHandler(AuthenticationEntryPointException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Response authenticationEntryPoint() {
    return Response.failure(-1001, "인증되지 않은 사용자입니다.");
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Response accessDeniedException() {
    return Response.failure(-1002, "접근이 거부되었습니다.");
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response MissingRequestHeaderException(MissingRequestHeaderException e) {
    return Response.failure(-1009, e.getHeaderName() + "요청 헤더가 누락되었습니다.");
  }
}
