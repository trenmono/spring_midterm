package com.example.midterm_java.controller;

import com.example.midterm_java.model.Product;
import com.example.midterm_java.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Integer id,
            @RequestBody Product product
    ) {
        Product existing = productRepository.findById(id).orElse(null);

        if (existing != null) {
            existing.setPName(product.getPName());
            existing.setPrice(product.getPrice());
            existing.setQty(product.getQty());
            existing.setExpireDate(product.getExpireDate());
            if (product.getProductCategory() != null) {
                existing.setProductCategory(product.getProductCategory());
            }
            return productRepository.save(existing);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

}
