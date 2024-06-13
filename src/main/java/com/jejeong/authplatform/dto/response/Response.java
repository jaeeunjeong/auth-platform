package com.jejeong.authplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(Include.NON_NULL)
public class Response {

  private boolean success;
  private int code;
  private Result result;

  public static Response success() {
    return new Response(true, 0, null);
  }

  public static <T> Response success(T data) {
    return new Response(true, 0, new Success<>(data));
  }

  public static Response failure(int code, String message) {
    return new Response(false, code, new Failure(message));
  }
}
