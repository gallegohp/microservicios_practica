package com.gallego.ms_auth.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gallego.ms_auth.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RequiredArgsConstructor
@Log4j2
@Component
public class JwtValidationFilter extends OncePerRequestFilter {
    
    /**
     * Servicio de jwt
     */
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        // Busca y obtiene en el encabezado de la peticion el header llamado
        // authotization
        String authHeader = request.getHeader("Authorization");
        
        // Un token legal debe existir y empezar con un "Bearer"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(("application/json"));
            response.getWriter().write("{\"error: \": Header Authorization is missing in the request\"}");
            return; //Cortamos el flujo y asi la peticion no alcanza a llegar al controller
        }

        String token = authHeader.replaceFirst("Bearer ", "");

        try {
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                Long userId = jwtService.extractUserId(token);
                Long rolId = jwtService.extractRolId(token);

                //Seteamos atributos en la peticion antes que llegue al controller para validarlos despues si es necesaario
                request.setAttribute("username", username);
                request.setAttribute("userId", userId);
                request.setAttribute("rolId", rolId );

                //Si todo sale vien, continuamos el flujo, ya sea con otro filtro o ya directamente
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token is invalid or expired\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Validation failed\"}");
            log.error("Error: " + e);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        //Estas rutas son publicas, es decir no entras al filtro de arriba
        return path.startsWith("/auth/login") || path.startsWith("/auth/register") || path.startsWith("/auth/refresh");
    }
}
