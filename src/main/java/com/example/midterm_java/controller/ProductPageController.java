package com.example.midterm_java.controller;

import com.example.midterm_java.model.Product;
import com.example.midterm_java.repository.CategoryRepository;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
//@RequiredArgsConstructor
public class ProductPageController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;

    public ProductPageController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductImageService productImageService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageService = productImageService;
    }

    @PostMapping("/save")
    public String saveProduct(
            @RequestParam(required = false) Integer id,
            @RequestParam String name,
            @RequestParam String quantity,
            @RequestParam double price,
            @RequestParam String expiredDate,
            @RequestParam(required = false) String imageName,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam Integer categoryId,
            @RequestParam(defaultValue = "ADMIN") String sourceRole,
            RedirectAttributes redirectAttributes
    ) {
        Product p = (id != null) ? productRepository.findById(id).orElse(new Product()) : new Product();
        p.setPName(name);
        p.setQty(quantity);
        p.setPrice(price);
        p.setExpireDate(expiredDate);
        p.setImageName(productImageService.resolveImageName(imageFile, imageName, p.getImageName(), name));

        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(p::setCategory);
        }

        productRepository.save(p);

        redirectAttributes.addFlashAttribute("toastMessage", "Product saved successfully!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return redirectTo(sourceRole, "products");
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "ADMIN") String sourceRole,
            RedirectAttributes redirectAttributes
    ) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "Product deleted!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return redirectTo(sourceRole, "products");
    }

    private String redirectTo(String sourceRole, String module) {
        String base = "STAFF".equalsIgnoreCase(sourceRole) ? "/staff/dashboard" : "/admin/dashboard";
        return "redirect:" + base + "?module=" + module;
    }
}
