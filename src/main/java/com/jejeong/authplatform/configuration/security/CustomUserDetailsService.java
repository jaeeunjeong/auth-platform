package com.jejeong.authplatform.configuration.security;

import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.repository.member.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Long userId = Long.valueOf(username);
    Member member = memberRepository.findById(userId)
        .orElseGet(() -> new Member(null, null, null, null,
            List.of()));
    return new CustomUserDetails(
        String.valueOf(member.getId()),
        member.getRoles().stream().map(memberRole -> memberRole.getRole())
            .map(role -> role.getRoleType())
            .map(roleType -> roleType.toString())
            .map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
    );
  }
}
