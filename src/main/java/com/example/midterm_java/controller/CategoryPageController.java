package com.example.midterm_java.controller;

import com.example.midterm_java.model.Category;
import com.example.midterm_java.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Category create/update/delete, posted to from both the admin and staff
 * dashboard forms (they pass sourceRole so we know which dashboard to
 * redirect back to).
 */
@Controller
@RequestMapping("/categories")
//@RequiredArgsConstructor
public class CategoryPageController {

    private final CategoryRepository categoryRepository;

    public CategoryPageController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/save")
    public String saveCategory(
            @RequestParam(required = false) Integer id,
            @RequestParam String name,
            @RequestParam(defaultValue = "ADMIN") String sourceRole,
            RedirectAttributes redirectAttributes
    ) {
        Category cat = (id != null) ? categoryRepository.findById(id).orElse(new Category()) : new Category();
        cat.setCategoryName(name);
        categoryRepository.save(cat);

        redirectAttributes.addFlashAttribute("toastMessage", "Category saved successfully!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return redirectTo(sourceRole);
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "ADMIN") String sourceRole,
            RedirectAttributes redirectAttributes
    ) {
        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "Category deleted!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return redirectTo(sourceRole);
    }

    private String redirectTo(String sourceRole) {
        String base = "STAFF".equalsIgnoreCase(sourceRole) ? "/staff/dashboard" : "/admin/dashboard";
        return "redirect:" + base + "?module=categories";
    }
}
