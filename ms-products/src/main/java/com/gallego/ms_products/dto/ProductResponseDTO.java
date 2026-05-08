package com.gallego.ms_products.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
}
