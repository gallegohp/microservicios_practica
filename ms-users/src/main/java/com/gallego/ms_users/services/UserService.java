package com.gallego.ms_users.services;

import java.util.List;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.gallego.ms_users.repository.UserRepository;
import com.gallego.ms_users.entity.User;
import com.gallego.ms_users.dto.*;

@Service
@RequiredArgsConstructor
public class UserService {
    
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    /**
     * 
     * @param userRequest
     * @return 
     */
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        
        // CAMBIO CLAVE: Encriptamos la contraseña usando BCrypt
        // En lugar de guardar "123", guardaremos algo como "$2a$10$..."
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(encodedPassword); 

        User savedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
    return response;
    }

    /**
     * 
     * @param id
     * @return 
     */
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
        
    }
    

    /**
     * 
     * @return 
     */
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponseDTO> listUsers = new ArrayList<>();
        for (User user : users) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
                
            listUsers.add(userResponseDTO);
        }
        return listUsers;
    }
    }
