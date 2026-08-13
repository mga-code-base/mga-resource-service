package com.mygym.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String requestUri = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");

        // 🔍 DEBUG LOGS: Intercept Incoming Path and Authorization Header existence
        log.info("🛡️ [JWT Filter] Intercepting request for URI: {}", requestUri);
        log.info("🛡️ [JWT Filter] Authorization Header Present: {}", (authHeader != null));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("⚠️ [JWT Filter] No Bearer token found in headers. Skipping extraction context for path: {}", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        try {
            final String username = jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(jwt, username)) {
                    // 🎯 FIXED: Direct extraction via custom claims set mapping
                    String role = jwtUtil.extractRole(jwt);
                    String systemAuthority = "ROLE_" + role;
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(systemAuthority))
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception ignored) {
        }

        
        filterChain.doFilter(request, response);
    }
}
