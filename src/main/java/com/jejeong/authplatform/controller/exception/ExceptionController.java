package com.jejeong.authplatform.controller.exception;

import com.jejeong.authplatform.exception.security.AccessDeniedException;
import com.jejeong.authplatform.exception.security.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

  @GetMapping("/exception/entry-point")
  public void entryPoint() {
    throw new AuthenticationEntryPointException();
  }

  @GetMapping("/exception/access-denied")
  public void accessDenied() {
    throw new AccessDeniedException();
  }
}
