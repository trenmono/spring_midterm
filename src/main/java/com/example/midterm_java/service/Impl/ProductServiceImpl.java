package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.ApiResponse;
import com.example.midterm_java.model.Product;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<Product>> addProduct(Product product) {
        if (!product.getPName().matches("^[a-zA-Z]+$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            400,
                            "Product name must be characters only",
                            null
                    ));
        }

        boolean checkProductName = productRepository.existsByPName(product.getPName());
        if (checkProductName) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(
                            false,
                            409,
                            "Product name already exists",
                            null
                    ));
        }

        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        201,
                        "Product created successfully",
                        saved
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Product not found",
                            null
                    ));
        }
        productRepository.delete(existingProduct.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Product deleted successfully",
                        null
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<Product>> updateProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Product not found",
                            null
                    ));
        }
        Product updatedProduct = existingProduct.get();
        updatedProduct.setPName(product.getPName());
        updatedProduct.setProductCategory(product.getProductCategory());
        Product savedProduct = productRepository.save(updatedProduct);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Product updated successfully",
                        savedProduct
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<Optional<Product>>> findByProductId(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Product not found",
                            null
                    ));
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ApiResponse<>(
                        true,
                        302,
                        "Product found",
                        product
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<List<Product>>> findByProductName(String name) {
        List<Product> products = productRepository.findByPName(name);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Product not found",
                            null
                    ));
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ApiResponse<>(
                        true,
                        202,
                        "Products found",
                        products
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<List<Product>>> findByPCategory(String category) {
        List<Product> products = productRepository.findByCategory();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Products not found",
                            null
                    ));
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ApiResponse<>(
                        true,
                        302,
                        "Products found",
                        products
                ));
    }

    @Override
    public ResponseEntity<List<Product>> findAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(products);
    }

}
