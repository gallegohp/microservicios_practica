package com.gallego.ms_users.dto;

import lombok.Data;

/**
 * DTO para respuesta de usuario
 */
@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
}
