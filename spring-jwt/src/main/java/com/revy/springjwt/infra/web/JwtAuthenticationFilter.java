package com.revy.springjwt.infra.web;

import com.revy.springjwt.service.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Cookie 또는 Header의 토큰정보 획득
        String cookieJWT = jwtService.getJwtFromCookies(request);
        final String authHeaderJWT = request.getHeader("Authorization");

        if ((StringUtils.isEmpty(cookieJWT) && (StringUtils.isEmpty(authHeaderJWT) || !authHeaderJWT.startsWith("Bearer "))) || request.getRequestURI().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        //  헤더에만 JWT 토큰이 있을경우, 보정
        if (StringUtils.isEmpty(cookieJWT) && authHeaderJWT.startsWith("Bearer ")) {
            cookieJWT = authHeaderJWT.substring(7); // after "Bearer "
        }

        // JWT에서 Email 주소 추출
        final String userEmail = jwtService.extractUserName(cookieJWT);


        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(cookieJWT, userDetails)) {

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }

        }
        filterChain.doFilter(request, response);
    }
}
