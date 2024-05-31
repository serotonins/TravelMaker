package com.ssafy.gumibom.global.config;

import com.ssafy.gumibom.domain.user.dto.JwtToken;
import com.ssafy.gumibom.domain.user.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = resolveToken((HttpServletRequest) request);

        // 토큰 유효성 검사
        if (token!=null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else { // 액세스 토큰이 유효하지 않은 경우
            String refreshToken = httpRequest.getHeader("Refresh-Token");
            if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateRefreshToken(refreshToken)) {
                // refresh token이 유효하면 새 액세스 토큰 발급
                JwtToken newTokens = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);

                // 새 access token을 authentication 객체에 설정하고 security context에 저장
                Authentication newAuthentication = jwtTokenProvider.getAuthentication(newTokens.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);

                // 새 access token과 refresh token을 응답 헤더에 추가
                httpResponse.setHeader("Authorization", "Bearer " + newTokens.getAccessToken());
                httpResponse.setHeader("Refresh-Token", newTokens.getRefreshToken());

            }
        }
        chain.doFilter(request, response);
    }

    // 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
