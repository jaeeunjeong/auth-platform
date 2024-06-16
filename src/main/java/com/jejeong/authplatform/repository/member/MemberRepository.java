package com.jejeong.authplatform.repository.member;

import com.jejeong.authplatform.entity.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  boolean existsByEmail(String email);

//  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByNickname(String nickname);
}
