package com.jejeong.authplatform.configuration.security;

import com.jejeong.authplatform.service.sign.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final TokenService tokenService;
  private final CustomUserDetailsService detailsService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String token = extractToken(request);
    String username = tokenService.extractRefreshTokenUsername(token);
    if (validateAccessToken(token, username)) {
      setAccessAuthentication("access", token);
    } else if (validateRefreshToken(token, username)) {
      setRefreshAuthentication("refresh", token);
    }
  }

  private String extractToken(ServletRequest request) {
    return ((HttpServletRequest) request).getHeader("Authorization");
  }

  private boolean validateAccessToken(String token, String username) {
    return token != null && tokenService.validateAccessToken(token, username);
  }

  private boolean validateRefreshToken(String token, String username) {
    return token != null && tokenService.validateRefreshToken(token, username);
  }

  private void setAccessAuthentication(String type, String token) {
    String userId = tokenService.extractAccessTokenSubject(token);
    CustomUserDetails userDetails = (CustomUserDetails) detailsService.loadUserByUsername(userId);
    SecurityContextHolder.getContext().setAuthentication(
        new CustomAuthenticationToken(type, userDetails, userDetails.getAuthorities()));

  }

  private void setRefreshAuthentication(String type, String token) {
    String userId = tokenService.extractRefreshTokenSubject(token);
    CustomUserDetails userDetails = (CustomUserDetails) detailsService.loadUserByUsername(userId);
    SecurityContextHolder.getContext().setAuthentication(
        new CustomAuthenticationToken(type, userDetails, userDetails.getAuthorities()));
  }

}
