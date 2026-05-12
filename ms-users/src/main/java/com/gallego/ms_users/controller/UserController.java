package com.gallego.ms_users.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import com.gallego.ms_users.services.UserService;
import com.gallego.ms_users.dto.*;
import com.gallego.ms_common.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    /**
     * Inyección de dependencias para UserService
     */
    private final UserService userService;
    
    /**
     * Inyección de dependencias para JwtService, utilizado para validar el token JWT en cada endpoint protegido.
     */
    private final JwtService jwtService;

    /**
     * Valida el token JWT del usuario antes de permitir el acceso a los endpoints protegidos.
     * @param request
     * @return boolean
     */
    private boolean isValidToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtService.isTokenValid(token);
    }

    /**
     * Crea un nuevo usuario. Solo accesible para usuarios autenticados.
     * @param userRequest
     * @param request
     * @return ResponseEntity<UserResponseDTO>
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequest, HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponseDTO createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Obtiene un usuario por su ID. Solo accesible para usuarios autenticados.
     * @param id
     * @param request
     * @return ResponseEntity<UserResponseDTO>
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Obtiene todos los usuarios. Solo accesible para usuarios autenticados.
     * @param request
     * @return ResponseEntity<List<UserResponseDTO>>
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}