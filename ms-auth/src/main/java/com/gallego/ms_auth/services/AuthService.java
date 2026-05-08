package com.gallego.ms_auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallego.ms_auth.dto.RegisterRequestDTO;
import com.gallego.ms_auth.dto.RegisterResponseDTO;
import com.gallego.ms_auth.entity.User;
import com.gallego.ms_auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    /**
     * Repositorio de usuarios
     */
    private final UserRepository userRepository;

    /**
     * Encriptacion de contraseñas
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Servicio de jwt
     */
    private final JwtService jwtService;

    /**
     * Registro de usuario
     * 
     * @param requestDTO
     * @return RegisterResponseDTO
     */
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO) {
        RegisterResponseDTO responseDTO = new RegisterResponseDTO();

        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            responseDTO.setMessage("El correo ya esta en uso");
            return responseDTO;
        }

        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword());
        user.setAge(requestDTO.getAge());
        user.setRolId(requestDTO.getRol());
        userRepository.save(user);

        responseDTO.setMessage("Se ha registrado correctamente");
        return responseDTO;
    }
}
