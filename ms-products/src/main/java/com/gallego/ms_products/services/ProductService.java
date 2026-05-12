package com.gallego.ms_products.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import com.gallego.ms_products.repository.ProductRepository;
import com.gallego.ms_products.entity.Product;
import com.gallego.ms_products.dto.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    
        /**
         * Inyección de dependencias para ProductRepository, utilizado para acceder a la base de datos y realizar operaciones CRUD sobre los productos.
         */
        private final ProductRepository productRepository;
    
        /**
         * Crea un nuevo producto en la base de datos a partir de los datos proporcionados en el ProductRequestDTO.
         * @param productRequest
         * @return ProductResponseDTO
         */
        public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            Product savedProduct = productRepository.save(product);

            ProductResponseDTO response = new ProductResponseDTO();
            response.setId(savedProduct.getId());
            response.setName(savedProduct.getName());
            response.setPrice(savedProduct.getPrice());
            response.setStock(savedProduct.getStock());
            return response;
        }
        
        /**
         * Obtener un producto en especifico, usando un id
         * 
         * @param id
         * @return ProductResponseDTO
         */ 
        public ProductResponseDTO getProductById(Long id) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            ProductResponseDTO response = new ProductResponseDTO();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setPrice(product.getPrice());
            return response;
        }
    
        /**
         * obtener todos los productos
         * 
         * @return listaProdutctos
         */
        public List<ProductResponseDTO> getAllProducts() {
            List<Product> products = productRepository.findAll();
            List<ProductResponseDTO> listProducts = new ArrayList<>();
            for (Product product : products) {
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                productResponseDTO.setId(product.getId());
                productResponseDTO.setName(product.getName());
                productResponseDTO.setStock(product.getStock());
                productResponseDTO.setPrice(product.getPrice());
                    
                listProducts.add(productResponseDTO);
            }
            return listProducts;
        }

        /**
         * Elimina un producto de la base de datos utilizando su ID. Solo accesible para usuarios con rol de administrador (rolId = 1).
         * @param id
         */
        public void deleteProduct(Long id) {
            productRepository.deleteById(id);
        }
}
