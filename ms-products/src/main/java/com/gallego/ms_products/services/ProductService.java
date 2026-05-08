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
    
        private final ProductRepository productRepository;
    
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
    
        public ProductResponseDTO getProductById(Long id) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            ProductResponseDTO response = new ProductResponseDTO();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setPrice(product.getPrice());
            response.setStock(product.getStock());
            return response;
        }
    
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
}
