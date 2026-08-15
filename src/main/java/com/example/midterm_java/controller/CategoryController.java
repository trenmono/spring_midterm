package com.example.midterm_java.controller;

import com.example.midterm_java.model.Category;
import com.example.midterm_java.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories(@RequestParam(required = false) String name) {
        if (name != null && !name.trim().isEmpty()) {
            return getCategoryByName(name);
        }
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @GetMapping("/name/{name}")
    public List<Category> getCategoryByName(@PathVariable String name) {
        List<Category> list = categoryRepository.findByCategoryName(name);
        if (list.isEmpty()) {
            list = categoryRepository.findByCategoryNameContainingIgnoreCase(name);
        }
        return list;
    }

    @GetMapping("/by-name/{name}")
    public List<Category> getCategoryByNamePath(@PathVariable String name) {
        return getCategoryByName(name);
    }

    @GetMapping("/search")
    public List<Category> searchCategoryByName(@RequestParam String name) {
        return getCategoryByName(name);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(
            @PathVariable Integer id,
            @RequestBody Category category
    ) {
        Category existing = categoryRepository.findById(id).orElse(null);

        if (existing != null) {
            existing.setCategoryName(category.getCategoryName());
            return categoryRepository.save(existing);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
        return "Category deleted successfully";
    }
}
