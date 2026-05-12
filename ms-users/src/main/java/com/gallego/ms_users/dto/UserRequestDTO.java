package com.gallego.ms_users.dto;

import lombok.Data;
@Data
public class UserRequestDTO {
    /**
     * nombre del usuario
     */
    private String name;
    /**
     * correo del usuario
     */
    private String email;
    /**
     * contraseña del usuario
     */
    private String password;
    /**
     * rol del usuario
     */
    private Long rolId;
    /**
     * edad del usuario
     */
    private Long age;
}
