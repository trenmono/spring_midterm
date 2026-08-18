package com.example.midterm_java.controller;

import com.example.midterm_java.model.Product;
import com.example.midterm_java.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
//@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts(@RequestParam(required = false) String name, @RequestParam(required = false) String search) {
        String query = (search != null && !search.trim().isEmpty()) ? search : name;
        if (query != null && !query.trim().isEmpty()) {
            return getProductByName(query);
        }
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @GetMapping("/name/{name}")
    public List<Product> getProductByName(@PathVariable String name) {
        List<Product> list = productRepository.findByPName(name);
        if (list.isEmpty()) {
            list = productRepository.findByPNameContainingIgnoreCase(name);
        }
        return list;
    }

    @GetMapping("/search/{name}")
    public List<Product> getProductByNamePath(@PathVariable String name) {
        return getProductByName(name);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(required = false) String name, @RequestParam(required = false) String search) {
        String query = (search != null && !search.trim().isEmpty()) ? search : name;
        if (query != null && !query.trim().isEmpty()) {
            return getProductByName(query);
        }
        return productRepository.findAll();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

}
