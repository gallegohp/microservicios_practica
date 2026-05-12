package com.gallego.ms_auth.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallego.ms_auth.dto.HttpGlobalResponse;
import com.gallego.ms_auth.dto.LoginRequestDTO;
import com.gallego.ms_auth.dto.RegisterRequestDTO;
import com.gallego.ms_auth.dto.RegisterResponseDTO;
import com.gallego.ms_auth.entity.User;
import com.gallego.ms_auth.repository.UserRepository;
import com.gallego.ms_common.dto.JwtDTO;
import com.gallego.ms_common.service.JwtService;

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
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setAge(requestDTO.getAge());
        user.setRolId(requestDTO.getRol());
        userRepository.save(user);

        responseDTO.setMessage("Se ha registrado correctamente");
        return responseDTO;
    }
    /**
     * Inicio de sesion
     * @param requestDTO
     * @return HttpGlobalResponse<JwtDTO>
     */
    public HttpGlobalResponse<JwtDTO> login(LoginRequestDTO requestDTO) {
        HttpGlobalResponse<JwtDTO> response = new HttpGlobalResponse<>();
        Optional<User> userFound = userRepository.findByEmail(requestDTO.getEmail());

        if (userFound.isEmpty()) {
            response.setMessege("Este usuario no se encuentra registrado");
            return response;
        }

        User user = userFound.get();

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            response.setMessege("Correo o contraseña son incorrectos");
            return response;
        }

        JwtDTO jwtDTO = new JwtDTO();
        String jwt = jwtService.generateToken(user.getId(),user.getRolId(), user.getEmail());
        jwtDTO.setJwt(jwt);
        response.setMessege("Inicio de sesion exitoso");
        response.setData(jwtDTO);
        return response;
    }

    /**
     * Refresco del jwt
     * @param token
     * @return JwtDTO
     * @throws Exception
     */
    public JwtDTO refreshToken(String token) throws Exception {
        JwtDTO responseDTO = new JwtDTO();
        String jwt = jwtService.refreshToken(token);
        responseDTO.setJwt(jwt);
        return responseDTO;
    }

}
