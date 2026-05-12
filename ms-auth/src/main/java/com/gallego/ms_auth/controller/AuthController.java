package com.gallego.ms_auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gallego.ms_auth.dto.HttpGlobalResponse;
import com.gallego.ms_auth.dto.LoginRequestDTO;
import com.gallego.ms_auth.dto.RegisterRequestDTO;
import com.gallego.ms_auth.dto.RegisterResponseDTO;
import com.gallego.ms_auth.services.AuthService;
import com.gallego.ms_common.dto.JwtDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    /**
     * Servicio de autenticacion
     */
    private final AuthService authService;

    /**
     * Registro de usuario
     * @param requestDTO
     * @return ResponseEntity<RegisterResponseDTO>
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO requestDTO) {
        try {
            RegisterResponseDTO responseDTO = authService.register(requestDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Inicio de sesion del usuario
     * 
     * @param request
     * @return HttpGlobalResponse<JwtDTO>
     */
    @PostMapping("/login")
    public ResponseEntity<HttpGlobalResponse<JwtDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            HttpGlobalResponse<JwtDTO> response = authService.login(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    /**
     * Refresco del jwt
     * 
     * @param request
     * @return JwtDTO
     */
    @GetMapping("/refresh")
    public ResponseEntity<JwtDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.replaceFirst("Bearer ", "");

        JwtDTO response = new JwtDTO();

        try {
            response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
