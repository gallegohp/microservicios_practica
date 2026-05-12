package com.gallego.ms_products.entity;

import jakarta.persistence.*;
import lombok.*;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products") // Aquí le das el nombre a la tabla
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Double price;
    private Integer stock;
}