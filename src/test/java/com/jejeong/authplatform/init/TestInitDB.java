package com.jejeong.authplatform.init;

import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.entity.member.Role;
import com.jejeong.authplatform.entity.member.RoleType;
import com.jejeong.authplatform.exception.member.RoleNotFoundException;
import com.jejeong.authplatform.repository.member.MemberRepository;
import com.jejeong.authplatform.repository.role.RoleRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestInitDB {

  @Autowired
  RoleRepository roleRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  private String adminEmail = "admin@admin.com";
  private String member1Email = "member1@member.com";
  private String member2Email = "member2@member.com";
  private String password = "1234!abcd";

  @Transactional
  public void initDB() {
    initRole();
    initTestAdmin();
    initTestMember();
  }

  private void initTestMember() {
    roleRepository.saveAll(
        List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(
            Collectors.toList()));
  }

  private void initTestAdmin() {
    memberRepository.save(
        new Member(adminEmail, passwordEncoder.encode(password), "admin", "admin",
            List.of(
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                    .orElseThrow(RoleNotFoundException::new),
                roleRepository.findByRoleType(RoleType.ROLE_ADMIN)
                    .orElseThrow(RoleNotFoundException::new)
            )
        )
    );
  }

  private void initRole() {
    roleRepository.saveAll(
        List.of(RoleType.values()).stream().map(roleType -> new Role(roleType))
            .collect(Collectors.toList()));
  }

  public String getAdminEmail() {
    return adminEmail;
  }

  public String getMember1Email() {
    return member1Email;
  }

  public String getMember2Email() {
    return member2Email;
  }

  public String getPassword() {
    return password;
  }
}
