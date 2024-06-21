package com.jejeong.authplatform.controller.member;

import com.jejeong.authplatform.dto.response.Response;
import com.jejeong.authplatform.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/api/members/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Response read(@PathVariable Long id) {
    return Response.success(memberService.read(id));
  }

  @DeleteMapping("/api/members/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Response delete(@PathVariable Long id) {
    memberService.delete(id);
    return Response.success();
  }
}
