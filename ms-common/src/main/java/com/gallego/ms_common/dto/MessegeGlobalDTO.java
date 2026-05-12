package com.gallego.ms_common.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MessegeGlobalDTO {
    
    /**
     * Mensaje global para respuestas de error o exito
     */
    private String message;

}
