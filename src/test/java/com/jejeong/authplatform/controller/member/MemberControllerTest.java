package com.jejeong.authplatform.controller.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jejeong.authplatform.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

  @InjectMocks
  MemberController memberController;
  @Mock
  MemberService memberService;
  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
  }

  @Test
  @DisplayName("회원 정보 조회 확인")
  void test1() throws Exception {
    Long id = 1L;
    mockMvc.perform(
            get("/api/members/{id}", id))
        .andExpect(status().isOk());
    verify(memberService).read(id);
  }

  @Test
  @DisplayName("회원 삭제 확인")
  void test2() throws Exception {
    Long id = 1L;

    mockMvc.perform(
            delete("/api/members/{id}", id))
        .andExpect(status().isOk());
    verify(memberService).delete(id);
  }
}