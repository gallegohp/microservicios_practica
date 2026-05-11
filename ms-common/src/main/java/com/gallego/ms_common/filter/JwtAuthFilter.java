package com.gallego.ms_common.filter;

import java.io.IOException;

import com.gallego.ms_common.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header missing or invalid");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Authorization header missing or invalid\"}");
            return;
        }
        
        try {
            String token = authHeader.substring(7);
            
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                Long userId = jwtService.extractUserId(token);
                Long rolId = jwtService.extractRolId(token);
                
                request.setAttribute("username", username);
                request.setAttribute("userId", userId);
                request.setAttribute("rolId", rolId);
                
                log.debug("Token válido para usuario: {}", username);
                filterChain.doFilter(request, response);
            } else {
                log.warn("Token inválido o expirado");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
            }
        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Token inválido\"}");
        }
    }
}