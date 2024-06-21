package com.jejeong.authplatform.controller.member;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jejeong.authplatform.dto.sign.SignInRequest;
import com.jejeong.authplatform.dto.sign.SignInResponse;
import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.exception.member.MemberNotFoundException;
import com.jejeong.authplatform.init.TestInitDB;
import com.jejeong.authplatform.repository.member.MemberRepository;
import com.jejeong.authplatform.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MemberControllerIntegrationTest {

  @Autowired
  WebApplicationContext context;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  TestInitDB testInitDB;

  @Autowired
  SignService signService;
  @Autowired
  MemberRepository memberRepository;

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders.standaloneSetup(context).apply(springSecurity()).build();
    testInitDB.initDB();
  }

  @Test
  @DisplayName("사용자 정보 조회하기")
  void test1() throws Exception {
    // given
    Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
        .orElseThrow(MemberNotFoundException::new);

    // when, then
    mockMvc.perform(
        get("/api/members/{id}", member.getId())
    ).andExpect(status().isOk());
  }

  @Test
  @DisplayName("삭제 확인")
  void test2() throws Exception {
    Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
        .orElseThrow(MemberNotFoundException::new);

    SignInResponse signInResponse = signService.signIn(
        new SignInRequest(testInitDB.getMember1Email(), testInitDB.getPassword()));

    mockMvc.perform(
        delete("/api/members/{id}", member.getId())
            .header("Authorization", signInResponse.getAccessToken())
    ).andExpect(status().isOk());
  }

  @Test
  @DisplayName("관리자에 의한 삭제")
  void test3() throws Exception {
    Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
        .orElseThrow(MemberNotFoundException::new);
    SignInResponse adminSignInRes = signService.signIn(new SignInRequest(testInitDB.getAdminEmail(),
        testInitDB.getPassword()));

    mockMvc.perform(
        delete("/api/members/{id}", member.getId())
            .header("Authorization", adminSignInRes.getAccessToken())
    ).andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증되지 않은 사용자 요청")
  void test4() throws Exception {
    Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
        .orElseThrow(MemberNotFoundException::new);

    mockMvc.perform(
            delete("/api/members/{id}", member.getId()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/expection/entry-point"));
  }

  @Test
  @DisplayName("인증된 사용자가 다른 사람의 자원에 접근할 경우")
  void test5() throws Exception {
    Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
        .orElseThrow(MemberNotFoundException::new);
    SignInResponse signInResponse = signService.signIn(
        new SignInRequest(testInitDB.getMember2Email(), testInitDB.getPassword()));

    mockMvc.perform(
            delete("/api/members/{id}", member.getId())
                .header("Authorization", signInResponse.getAccessToken()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/exception/access-denied"));
  }

}
