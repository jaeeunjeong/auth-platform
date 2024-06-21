package com.jejeong.authplatform.service.sign;

import com.jejeong.authplatform.dto.sign.RefreshTokenResponse;
import com.jejeong.authplatform.dto.sign.SignInRequest;
import com.jejeong.authplatform.dto.sign.SignInResponse;
import com.jejeong.authplatform.dto.sign.SignUpRequest;
import com.jejeong.authplatform.entity.member.Member;
import com.jejeong.authplatform.entity.member.Role;
import com.jejeong.authplatform.entity.member.RoleType;
import com.jejeong.authplatform.exception.member.RoleNotFoundException;
import com.jejeong.authplatform.exception.security.AuthenticationEntryPointException;
import com.jejeong.authplatform.exception.sign.LoginFailureException;
import com.jejeong.authplatform.exception.sign.MemberEmailAlreadyExistException;
import com.jejeong.authplatform.exception.sign.MemberNicknameAlreadyExistException;
import com.jejeong.authplatform.repository.member.MemberRepository;
import com.jejeong.authplatform.repository.role.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {

  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;


  @Transactional
  public void signUp(SignUpRequest req) {
    validateSignUpInfo(req);
    List<Role> roles = List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
        .orElseThrow(RoleNotFoundException::new));
    String encodePassword = passwordEncoder.encode(req.getPassword());
    memberRepository.save(new Member(req.getEmail(), encodePassword, req.getUsername(),
        req.getNickname(), roles));
  }

  @Transactional(readOnly = true)
  public SignInResponse signIn(SignInRequest req) {
    Member savedMember = memberRepository.findByEmail(req.getEmail())
        .orElseThrow(LoginFailureException::new);
    validatePassword(savedMember, req);
    String accessToken = tokenService.generateAccessToken(savedMember.getUsername());
    String refreshToken = tokenService.generateRefreshToken(savedMember.getUsername());
    return new SignInResponse(accessToken, refreshToken);
  }

  private void validatePassword(Member savedMember, SignInRequest req) {
    if (!passwordEncoder.matches(savedMember.getPassword(), req.getPassword())) {
      throw new LoginFailureException();
    }
  }

  private void validateSignUpInfo(SignUpRequest req) {
    if (memberRepository.existsByEmail(req.getEmail())) {
      throw new MemberEmailAlreadyExistException(req.getEmail());
    }
    if (memberRepository.existsByNickname(req.getNickname())) {
      throw new MemberNicknameAlreadyExistException(req.getNickname());
    }
  }

  public RefreshTokenResponse refreshToken(String refreshToken) {
    validateRefreshToken(refreshToken);
    String subject = tokenService.extractRefreshTokenSubject(refreshToken);
    String accessToken = tokenService.generateAccessToken(subject);
    return new RefreshTokenResponse(accessToken);
  }

  private void validateRefreshToken(String refreshToken) {
    String username = "";
    if (!tokenService.validateRefreshToken(refreshToken, username)) {
      throw new AuthenticationEntryPointException();
    }
  }
}