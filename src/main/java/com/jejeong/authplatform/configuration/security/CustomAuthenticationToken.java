package com.jejeong.authplatform.configuration.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

  private String type;
  private CustomUserDetails principal;

  public CustomAuthenticationToken(
      String type,
      CustomUserDetails principal,
      Collection<? extends GrantedAuthority> authorities
  ) {
    super(authorities);
    this.type = type;
    this.principal = principal;
    setAuthenticated(true);
  }

  @Override
  public CustomUserDetails getCredentials() {
    return principal;
  }

  @Override
  public Object getPrincipal() {
    throw new UnsupportedOperationException();
  }
}
