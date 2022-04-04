package com.board.weare.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // JWT 토큰 확인 로직
        String token = tokenProvider.getToken((HttpServletRequest) request);

        if(token != null && tokenProvider.validateToken(token))
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(token));

        chain.doFilter(request, response);
    }
}
