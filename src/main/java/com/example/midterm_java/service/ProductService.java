package com.example.midterm_java.service;

import com.example.midterm_java.model.ApiResponse;
import com.example.midterm_java.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ResponseEntity<ApiResponse<Product>> addProduct(Product product);

    ResponseEntity<ApiResponse<Void>> deleteProduct(Product product);

    ResponseEntity<ApiResponse<Product>> updateProduct(Product product);

    ResponseEntity<ApiResponse<Optional<Product>>> findByProductId(Integer id);

    ResponseEntity<ApiResponse<List<Product>>> findByProductName(String name);

    ResponseEntity<ApiResponse<List<Product>>> findByPCategory(String category);

    ResponseEntity<List<Product>> findAllProducts();

}
