package com.jejeong.authplatform.configuration.security;

import com.jejeong.authplatform.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenService tokenService;
  private final CustomUserDetailsService userDetailsService;

  @Bean
  public WebSecurityCustomizer configure() {
    return (web) -> web.ignoring().requestMatchers("/exception/**");
  }

  @Bean
  public DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);
    http.sessionManagement(
        manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authorizeHttpRequests(request -> request
        .requestMatchers(HttpMethod.POST, "/api/sign-in", "/api/sign-up", "/api/refresh-token")
        .permitAll()
        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/members/{id}/**").authenticated()
        .anyRequest().hasAnyRole("ADMIN"));
    http.exceptionHandling(handling -> handling
        .accessDeniedHandler(new CustomAccessDeniedHandler())
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

    http.addFilter(new JwtAuthenticationFilter(tokenService, userDetailsService));
    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
