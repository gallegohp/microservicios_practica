package com.gallego.ms_products.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {
    
    private String name;
    private Double price;
    private Integer stock;
}
