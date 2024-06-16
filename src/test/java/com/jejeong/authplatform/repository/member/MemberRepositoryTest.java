package com.jejeong.authplatform.repository.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.entity.member.MemberRole;
import com.jejeong.authplatform.entity.member.Role;
import com.jejeong.authplatform.entity.member.RoleType;
import com.jejeong.authplatform.exception.member.MemberNotFoundException;
import com.jejeong.authplatform.repository.role.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  RoleRepository roleRepository;
  @PersistenceContext
  EntityManager entityManager;

  private void clear() {
    entityManager.flush();
    entityManager.clear();
  }

  @DisplayName("회원가입 확인")
  @Test
  void test1() {

    // given
    Member member = createMember();

    // when
    memberRepository.save(member);
    clear();

    // then
    Member foundMember = memberRepository.findByEmail(member.getEmail())
        .orElseThrow(MemberNotFoundException::new);
    assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
  }

  @DisplayName("회원가입 시간 확인")
  @Test
  void test2() {
    // given
    Member originMember = createMember();

    // when
    Member saved = memberRepository.save(originMember);
    clear();

    // then
    assertThat(saved.getCreateAt()).isNotNull();
    assertThat(saved.getModifiedAt()).isNotNull();
    assertThat(saved.getCreateAt()).isEqualTo(saved.getModifiedAt());
  }

  @DisplayName("닉네임 변경 확인 확인")
  @Test
  void test3() {

    // given
    Member originMember = createMember();
    memberRepository.save(originMember);

    // when
    String updatedNickname = "newSong";
    originMember.updateNickname(updatedNickname);
    clear();

    // then
    Member foundMember = memberRepository.findByEmail(originMember.getEmail())
        .orElseThrow(MemberNotFoundException::new);
    assertThat(foundMember.getNickname()).isEqualTo(updatedNickname);

  }

  @DisplayName("회원 삭제 확인")
  @Test
  void test4() {
    // given
    Member originMember = createMember();
    memberRepository.save(originMember);
    clear();

    // when
    memberRepository.delete(originMember);
    clear();

    // then
    assertThatThrownBy(() -> memberRepository.findByEmail(originMember.getEmail())
        .orElseThrow(MemberNotFoundException::new))
        .isInstanceOf(MemberNotFoundException.class);

  }

  @DisplayName("이메일 찾기 확인")
  @Test
  void test5() {
    // given
    Member originMember = createMember();
    memberRepository.save(originMember);
    clear();

    // when, then
    Member foundMember = memberRepository.findByEmail(originMember.getEmail())
        .orElseThrow(MemberNotFoundException::new);
    assertThat(foundMember.getEmail()).isEqualTo(originMember.getEmail());
    assertThat(foundMember.getId()).isEqualTo(originMember.getId());
  }

  @DisplayName("이메일 중복 확인")
  @Test
  void test6() {

    // given
    Member originMember = createMember();
    memberRepository.save(originMember);
    clear();

    // when, then
    boolean existsByEmail = memberRepository.existsByEmail(originMember.getEmail());
    assertThat(existsByEmail).isTrue();
  }

  @DisplayName("이메일이 중복될 경우 회원 가입 불가 처리")
  @Test
  void test7() {
    // given
    Member originMember = createMember();
    memberRepository.save(originMember);

    // when, then
    Member otherMember = new Member("email2@email.com", "password!@12", "hong gildong", "gildong",
        List.of());
    assertThatThrownBy(() -> memberRepository.save(otherMember)).isInstanceOf(
        DataIntegrityViolationException.class);


  }

  @DisplayName("회원 등급 같이 저장되는지 확인")
  @Test
  void test8() {
    // given
    List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_ADMIN);
    List<Role> roles = roleTypes.stream()
        .map(Role::new).collect(Collectors.toList());
    roleRepository.saveAll(roles);
    clear();

    // when
    Member originMember = new Member("asdf@gmail.com", "password!@12", "hong gildong", "gildong",
        roleRepository.findAll());
    memberRepository.save(originMember);
    clear();

    // then
    List<MemberRole> result = entityManager.createQuery("select mr from MemberRole mr",
            MemberRole.class)
        .getResultList();
    assertThat(result.size()).isEqualTo(roles.size());

  }

  @DisplayName("회원 삭제시 등급도 같이 삭제되는지 확인")
  @Test
  void test9() {
    // given
    List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_ADMIN);
    List<Role> roles = roleTypes.stream()
        .map(Role::new).collect(Collectors.toList());
    roleRepository.saveAll(roles);
    clear();

    Member originMember = new Member("asdf@gmail.com", "password!@12", "hong gildong", "gildong",
        roleRepository.findAll());
    memberRepository.save(originMember);
    clear();

    // when
    memberRepository.delete(originMember);
    clear();

    // then
    List<MemberRole> result = entityManager.createQuery("select mr from MemberRole mr",
            MemberRole.class)
        .getResultList();
    assertThat(result.size()).isZero();
  }

  private Member createMember() {
    return new Member("asdf@gmail.com", "password!@12", "hong gildong", "gildong",
        List.of());
  }
}