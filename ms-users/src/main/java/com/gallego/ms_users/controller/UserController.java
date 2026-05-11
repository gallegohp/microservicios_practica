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
    
    private final UserService userService;
    private final JwtService jwtService;

    private boolean isValidToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtService.isTokenValid(token);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequest, HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponseDTO createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

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