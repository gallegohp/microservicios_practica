package com.gallego.ms_products.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    
    /**
     * Id del producto
     */
    private Long id;
    /**
     * Nombre del producto
     */
    private String name;
    /**
     * Precio del producto
     */
    private Double price;
    /**
     * Stock del producto
     */
    private Integer stock;
}
