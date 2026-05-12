package com.gallego.ms_products.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import com.gallego.ms_products.services.ProductService;
import com.gallego.ms_products.dto.*;
import com.gallego.ms_common.dto.MessegeGlobalDTO;
import com.gallego.ms_common.service.JwtService;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ProductController {

    /**
     * Inyección de dependencias para ProductService
     */
    private final ProductService productService;
    
    /**
     * Inyección de dependencias para JwtService, utilizado para validar el token JWT en cada endpoint protegido.
     */
    private final JwtService jwtService;

    /**
     * Inyección de dependencias para MessegeGlobalDTO, utilizado para enviar mensajes globales en las respuestas de los endpoints.
     */
    private final MessegeGlobalDTO messegeGlobalDTO;

    /**
     * Valida el token JWT del usuario antes de permitir el acceso a los endpoints protegidos.
     * @param request
     * @return boolean
     */
    private boolean isValidToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ") ) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtService.isTokenValid(token);
    }

    /**
     * Valida si el usuario tiene rol de administrador (rolId = 1) antes de permitir el acceso a los endpoints protegidos para administradores.
     * @param request
     * @return boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtService.extractRolId(token) == 1;
    }

    /**
     * Crea un nuevo producto. Solo accesible para usuarios con rol de administrador (rolId = 1).
     * @param productRequest
     * @param request
     * @return ResponseEntity<ProductResponseDTO>
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequest, HttpServletRequest request)  {
        try {
            if (!isValidToken(request)) {
            messegeGlobalDTO.setMessage("Token inválido o no proporcionado. Acceso denegado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (!isAdmin(request)) {
                messegeGlobalDTO.setMessage("Acceso denegado. Solo los administradores pueden crear productos.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            ProductResponseDTO createdProduct = productService.createProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    /**
     * Obtiene un producto por su ID. Solo accesible para usuarios autenticados.
     * @param id
     * @param request
     * @return ResponseEntity<ProductResponseDTO>
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id, HttpServletRequest request) {
        try {
            if (!isValidToken(request)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            ProductResponseDTO product = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
    * Obtiene todos los productos. Solo accesible para usuarios autenticados.
    * @param request
    * @return ResponseEntity<List<ProductResponseDTO>>
    */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(HttpServletRequest request) {
        try {
            if (!isValidToken(request)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<ProductResponseDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Elimina un producto por su ID. Solo accesible para usuarios con rol de administrador (rolId = 1). 
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessegeGlobalDTO> deleteProductById(@PathVariable Long id, HttpServletRequest request) {
        try {
            if (!isValidToken(request)) {
                messegeGlobalDTO.setMessage("Token inválido o no proporcionado. Acceso denegado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messegeGlobalDTO);
            }
            if (!isAdmin(request)) {
                messegeGlobalDTO.setMessage("Acceso denegado. Solo los administradores pueden eliminar productos.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messegeGlobalDTO);
            }
            productService.deleteProduct(id);
            messegeGlobalDTO.setMessage("Producto eliminado exitosamente.");
            return ResponseEntity.ok(messegeGlobalDTO);
        } catch (Exception e) {
            e.printStackTrace();
            messegeGlobalDTO.setMessage("Error al eliminar el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messegeGlobalDTO);
        }

    }
    
}