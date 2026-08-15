package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.ApiResponse;
import com.example.midterm_java.model.Category;
import com.example.midterm_java.model.Product;
import com.example.midterm_java.repository.CategoryRepository;
import com.example.midterm_java.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<List<Category>>> findByCategories(String Name) {
        List<Category> categories = categoryRepository.findByCategoryName(Name);
        if (categories.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "No categories found with the name: " + Name,
                            new ArrayList<>()
                    ));
        }
        return ResponseEntity.status(200)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Categories retrieved successfully",
                        categories
                ));
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(categories);
    }

    @Override
    public ResponseEntity<ApiResponse<List<Category>>> addCategory(Category category) {
        if (!category.getCategoryName().matches("^[a-zA-Z]+$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    400,
                                    "Invalid category name. Please use only letters.",
                                    null
                            )
                    );
        }
        boolean checkCategoryName = categoryRepository.existsByCategoryName(category.getCategoryName());
        if (checkCategoryName) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse<>(
                            false,
                            409,
                            "Category name already exists.",
                            null
                    ));
        }

        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                                true,
                                200,
                                "Category created successfully",
                                null
                        )
                );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCategory(Category category) {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryId(category.getCatId());
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                           false,
                           404,
                           "Invalid category" ,
                            null
                    )
            );
        }
        categoryRepository.delete(category);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Category deleted successfully",
                        null
                ));



    }

    @Override
    public ResponseEntity<ApiResponse<List<Category>>> updateCategory(Category category) {
        Optional<Category> existingCategory = categoryRepository.findByCategoryId(category.getCatId());
        if (existingCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    404,
                                    "Category not found",
                                    null
                            )
                    );
        }
        Category updatedCategory = existingCategory.get();
        updatedCategory.setCategoryName(category.getCategoryName());
        categoryRepository.save(updatedCategory);
        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Category updated successfully",
                        null
                ));

    }
}
