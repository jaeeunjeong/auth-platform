package com.jejeong.authplatform.service.sign;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.jejeong.authplatform.dto.sign.SignInRequest;
import com.jejeong.authplatform.dto.sign.SignInResponse;
import com.jejeong.authplatform.dto.sign.SignUpRequest;
import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.entity.member.Role;
import com.jejeong.authplatform.entity.member.RoleType;
import com.jejeong.authplatform.exception.sign.LoginFailureException;
import com.jejeong.authplatform.exception.sign.MemberEmailAlreadyExistException;
import com.jejeong.authplatform.exception.sign.MemberNicknameAlreadyExistException;
import com.jejeong.authplatform.repository.member.MemberRepository;
import com.jejeong.authplatform.repository.role.RoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

  @InjectMocks
  SignService signService;
  @Mock
  MemberRepository memberRepository;
  @Mock
  RoleRepository roleRepository;
  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  TokenService tokenService;

  @Test
  @DisplayName("회원가입 확인")
  void test1() {
    // given
    SignUpRequest req = createSignUpRequest();
    given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(
        Optional.of(new Role((RoleType.ROLE_NORMAL))));

    // when
    signService.signUp(req);

    // then
    verify(passwordEncoder).encode(req.getPassword());
    verify(memberRepository).save(any());
  }

  @Test
  @DisplayName("중복된 이메일이 있는지 확인")
  void test2() {

    // given
    given(memberRepository.existsByEmail(anyString())).willReturn(true);

    // when
    assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
        .isInstanceOf(MemberEmailAlreadyExistException.class);

  }

  @Test
  @DisplayName("중복 닉네임 확인")
  void test3() {
    given(memberRepository.existsByNickname(anyString())).willReturn(true);

    // when, then
    assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
        .isInstanceOf(MemberNicknameAlreadyExistException.class);
  }

  @Test
  @DisplayName("로그인 테스트")
  void test4() {
    given(memberRepository.findByEmail(any())).willReturn(Optional.of(createSignIn()));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    given(tokenService.generateAccessToken(anyString())).willReturn("access");
    given(tokenService.generateRefreshToken(anyString())).willReturn("refresh");

    SignInResponse res = signService.signIn(new SignInRequest("email", "password"));

    assertThat(res.getAccessToken()).isEqualTo("access");
    assertThat(res.getRefreshToken()).isEqualTo("refresh");
  }

  @Test
  @DisplayName("회원이 아닌 경우")
  void test5() {
    given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

    assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
        .isInstanceOf(LoginFailureException.class);
  }

  @Test
  @DisplayName("비밀번호가 잘못된 경우")
  void test6() {
    given(memberRepository.findByEmail(any())).willReturn(Optional.of(createSignIn()));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

    assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
        .isInstanceOf(LoginFailureException.class);
  }

  SignUpRequest createSignUpRequest() {
    return new SignUpRequest("email@email.com", "password", "username", "nickname");
  }

  private Member createSignIn() {
    return new Member("asdf@gmail.com", "password!@12", "hong gildong", "gildong", emptyList());
  }

}