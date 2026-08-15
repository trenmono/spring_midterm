package com.example.midterm_java.service;

import com.example.midterm_java.model.ApiResponse;
import com.example.midterm_java.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<ApiResponse<List<Category>>> findByCategories(String categories);

    ResponseEntity<List<Category>> getAllCategories();

    ResponseEntity<ApiResponse<List<Category>>> addCategory(Category category);

    ResponseEntity<ApiResponse<Void>> deleteCategory(Category category);

    ResponseEntity<ApiResponse<List<Category>>> updateCategory(Category category);

}
