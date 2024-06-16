package com.jejeong.authplatform.controller.sign;

import com.jejeong.authplatform.dto.response.Response;
import com.jejeong.authplatform.dto.sign.SignInRequest;
import com.jejeong.authplatform.dto.sign.SignUpRequest;
import com.jejeong.authplatform.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {

  private final SignService signService;

  @PostMapping("/auth/signUp")
  public Response signUp(SignUpRequest req) {
    signService.signUp(req);
    return Response.success();
  }


  @PostMapping("/auth/signIn")
  public Response signIn(SignInRequest req) {
    return Response.success(signService.signIn(req));
  }

//  @PostMapping("/auth/signOut")
//  public Response signOut(SignOutRequest req){
//    signService.signOut
//    return Response.success();
//  }
//  @GetMapping("/auth/token")
//  public Response reissuanceToken() {
//    return Response.success();
//  }
}
