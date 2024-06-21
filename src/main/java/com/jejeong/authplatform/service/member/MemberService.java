package com.jejeong.authplatform.service.member;

import com.jejeong.authplatform.dto.member.MemberDto;
import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.exception.member.MemberNotFoundException;
import com.jejeong.authplatform.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  // 읽기
  public MemberDto read(Long id) {
    Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    return MemberDto.toDto(member);
  }

  // 삭제
  public void delete(Long id) {
    Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    memberRepository.delete(member);
  }
}
