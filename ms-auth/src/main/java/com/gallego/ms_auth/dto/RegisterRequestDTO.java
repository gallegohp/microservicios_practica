package com.gallego.ms_auth.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    /**
     * Nombre del usuario
     */
    private String name;

    /**
     * Email del usuario
     */
    private String email;

    /**
     * Contraseña del usuario
     */
    private String password;

    /**
     * Edad del usuario
     */
    private Long age;

    /**
     * Rol del usuario
     */
    private Long rol;
}
