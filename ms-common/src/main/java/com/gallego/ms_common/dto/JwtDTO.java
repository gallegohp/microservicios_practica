package com.gallego.ms_common.dto;

import lombok.Data;

@Data
public class JwtDTO {

    /**
     * JWT del usuario logueado
     */
    private String jwt;
}