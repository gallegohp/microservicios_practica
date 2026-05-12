package com.gallego.ms_users.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    /**
     * id del usuario
     */
    private Long id;
    /**
     * nombre del usuario
     */
    private String name;
    /**
     * correo del usuario
     */
    private String email;
}
