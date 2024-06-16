package com.jejeong.authplatform.entity.member;

import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jejeong.authplatform.entity.common.EntityDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@SQLDelete(sql = "UPDATE \"member\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityDate implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, length = 30, unique = true)
  private String email;

  private String password;

  @Column(nullable = false, length = 20)
  private String username;

  @Column(nullable = false, length = 20, unique = true)
  private String nickname;

//  @Column(nullable = false, length = 20, unique = true)
//  private String phoneNumber;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true) // 4
  private Set<MemberRole> roles;

  @Column(name = "registered_at")
  private Timestamp registeredAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Column(name = "removed_at")
  private Timestamp removedAt;

  @PrePersist
  void registeredAt() {
    this.registeredAt = Timestamp.from(Instant.now());
  }

  @PreUpdate
  void updatedAt() {
    this.updatedAt = Timestamp.from(Instant.now());
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return removedAt == null;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return removedAt == null;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return removedAt == null;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return removedAt == null;
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(roles.toString()));
  }

  public Member(String email, String password, String username, String nickname, List<Role> roles) {
    this.email = email;
//    this.phoneNumber = phonenumber;
    this.password = password;
    this.username = username;
    this.nickname = nickname;
    this.roles = roles.stream().map(r -> new MemberRole(this, r)).collect(toSet());
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

}
