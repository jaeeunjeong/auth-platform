package com.jejeong.authplatform.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {

  public String doGenerateToken(String username, long expireTime, String encodingKey) {
    Claims claims = Jwts.claims();
    claims.put("username", username);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expireTime))
        .signWith(SignatureAlgorithm.HS256, encodingKey)
        .compact();
  }

  public Claims extractAllClaims(String token, String encodingKey) {
    return Jwts.parserBuilder()
        .setSigningKey(encodingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean validate(String token, String username, String encodingKey) {
    String usernameByToken = getUsername(token, encodingKey);
    return usernameByToken.equals(username) && !isTokenExpired(token, encodingKey);
  }

  public boolean isTokenExpired(String token, String encodingKey) {
    Date expiration = extractAllClaims(token, encodingKey).getExpiration();
    return expiration.before(new Date());
  }

  public String getUsername(String token, String encodingKey) {
    return extractAllClaims(token, encodingKey).get("username", String.class);
  }


}
