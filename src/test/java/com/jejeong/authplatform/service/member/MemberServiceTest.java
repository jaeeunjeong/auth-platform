package com.jejeong.authplatform.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.jejeong.authplatform.dto.member.MemberDto;
import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.exception.member.MemberNotFoundException;
import com.jejeong.authplatform.repository.member.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @InjectMocks
  MemberService memberService;
  @Mock
  MemberRepository memberRepository;


  @Test
  @DisplayName("회원 정보 조회")
  void test1() {
    // given
    Member member = createMember();
    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

    // when
    MemberDto foundMember = memberService.read(1L);
    // then
    assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());

  }

  @Test
  @DisplayName("회원이 없는 경우 예외 처리")
  void test2() {
    // given
    given(memberRepository.findById(any())).willReturn(Optional.ofNullable(null));

    // when, then
    assertThatThrownBy(() -> memberService.read(1L)).isInstanceOf(MemberNotFoundException.class);

  }

  @Test
  @DisplayName("회원 정보 삭제")
  void test3() {
    // given
    Member member = createMember();
    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

    // when
    memberService.delete(anyLong());

    // then
    verify(memberRepository).delete(any());

  }

  @Test
  @DisplayName("회원이 없는 경우 예외 처리")
  void test4() {
//    given(memberRepository.existsById(anyLong())).willReturn(false);

    assertThatThrownBy(() -> memberService.delete(1L)).isInstanceOf(MemberNotFoundException.class);

  }

  private Member createMember() {
    return new Member("email@email.com", "password", "username", "nick", List.of());
  }
}