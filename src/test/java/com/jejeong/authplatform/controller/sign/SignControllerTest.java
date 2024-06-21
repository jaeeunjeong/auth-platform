package com.jejeong.authplatform.controller.sign;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jejeong.authplatform.dto.sign.RefreshTokenResponse;
import com.jejeong.authplatform.dto.sign.SignInRequest;
import com.jejeong.authplatform.dto.sign.SignInResponse;
import com.jejeong.authplatform.dto.sign.SignUpRequest;
import com.jejeong.authplatform.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class SignControllerTest {

  @InjectMocks
  SignController signController;
  @Mock
  SignService signService;
  MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
  }

  @Test
  @DisplayName("회원가입 확인")
  void test1() throws Exception {
    SignUpRequest signUpRequest = new SignUpRequest("email@email.com", "password", "username",
        "nickname");

    mockMvc.perform(
        post("/auth/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest))
    ).andExpect(status().isOk());

    verify(signService).signUp(signUpRequest);
  }

  @Test
  @DisplayName("로그인 확인")
  void test2() throws Exception {
    // given
    SignInRequest req = new SignInRequest("email", "password");
    given(signService.signIn(req)).willReturn(new SignInResponse("access", "refresh"));

    // when, then
    mockMvc.perform(
            post("/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.result.data.accessToken").value("access"))
        .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

    verify(signService).signIn(req);
  }

  @Test
  @DisplayName("토큰 재생성 확인")
  void test3() throws Exception {
    given(signService.refreshToken("refreshToken")).willReturn(
        new RefreshTokenResponse("accessToken"));

    mockMvc.perform(
            post("/api/refresh-token")
                .header("Authoriaztion", "refreshToken")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.result.data.accessToken").value("accessToken"));
  }

}