package com.revy.springjwt.service.impl;

import com.revy.springjwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final long COOKIE_AGE = 24 * 60 * 60;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.cookie-name}")
    private String jwtCookieName;


    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);

        if (StringUtils.isEmpty(userName)) {
            //TODO:REVY - 적당한 Exception 만들어서 던져주자
            throw new RuntimeException("userName이 없습니다.");
        }

        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    @Override
    public ResponseCookie generateJwtCookie(String jwt) {
        /*
        None, Lax, Strict 세가지 종류가 있습니다.
        None : 기존의 방식과 동일힙니다. 서드 파티 쿠키가 전송됩니다.
        Lax : 몇가지 예외적인 요청을 제외하고는 서트 파티쿠키가 전송되지 않습니다.
        Strict : 항상 서드파티 쿠키는 전송되지 않고 퍼스트파티 쿠키만 전송됩니다.
         */
        return ResponseCookie.from(this.jwtCookieName, jwt)
                .path("/")
                .maxAge(this.COOKIE_AGE)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict").build();
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return cookie == null ? null : cookie.getValue();
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(this.jwtCookieName, "")
                .path("/")
                .build();
    }


    // ### PRIVATE AREA ###

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);

    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, this.jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
