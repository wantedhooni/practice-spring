package com.revy.springjwt.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    /**
     * JWT token에서 UserName을 반환한다.
     * @param token
     */
    String extractUserName(String token);

    /**
     * userDetails으로 JWT 토큰을 생성한다.
     * @param userDetails
     */
    String generateToken(UserDetails userDetails);

    /**
     * 토큰 유효성을 검증한다.
     * @param token
     * @param userDetails
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * jwt 쿠키를 생성한다.
     * @param jwt
     * @return
     */
    ResponseCookie generateJwtCookie(String jwt);

    /**
     * request 쿠키에서 jwt 토큰을 추출한다.
     * @param request
     * @return
     */
    String getJwtFromCookies(HttpServletRequest request);

    /**
     * ResponseCookie의 jwt 필드를 empty로 변경한다.
     */
    ResponseCookie getCleanJwtCookie();
}
