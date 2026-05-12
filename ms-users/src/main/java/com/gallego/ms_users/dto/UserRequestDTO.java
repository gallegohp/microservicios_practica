package com.gallego.ms_users.dto;

import lombok.Data;

/**
 * DTO para solicitud de creación de usuario
 */
@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private Long rolId;
    private Long age;
}
