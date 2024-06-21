package com.jejeong.authplatform.dto.member;

import com.jejeong.authplatform.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {

  public MemberDto() {
  }

  private Long id;
  private String email;
  private String username;
  private String nickname;

  public static MemberDto toDto(Member member) {
    return new MemberDto(member.getId(),
        member.getEmail(),
        member.getUsername(),
        member.getEmail());
  }
}
