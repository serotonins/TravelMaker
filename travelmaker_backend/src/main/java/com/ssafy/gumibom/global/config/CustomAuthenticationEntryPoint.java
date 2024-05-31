package com.ssafy.gumibom.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
        // 만료된 토큰으로 인증을 시도했을 때 401 상태 코드 반환
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied: Unauthorized");
    }
}
