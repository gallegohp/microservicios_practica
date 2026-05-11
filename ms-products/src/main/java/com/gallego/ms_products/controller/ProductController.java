package com.gallego.ms_products.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import com.gallego.ms_products.services.ProductService;
import com.gallego.ms_products.dto.*;
import com.gallego.ms_common.service.JwtService;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final JwtService jwtService;

    /**
     * Valida el token JWT del usuario antes de permitir el acceso a los endpoints protegidos.
     * @param request
     * @return
     */
    private boolean isValidToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtService.isTokenValid(token);
    }

    /**
     * Crea un nuevo producto. Solo accesible para usuarios con rol de administrador (rolId = 1).
     * @param productRequest
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequest, HttpServletRequest request) {
        if (!isValidToken(request) || jwtService.extractRolId(request.getHeader("Authorization").substring(7)) != 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProductResponseDTO createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id, HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

}