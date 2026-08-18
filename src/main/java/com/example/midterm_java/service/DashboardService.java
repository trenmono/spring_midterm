package com.example.midterm_java.service;

import com.example.midterm_java.model.CategorySalesDTO;
import com.example.midterm_java.model.Product;
import com.example.midterm_java.model.Staff;

import java.util.List;

public interface DashboardService {

    /**
     * Get the top-selling staff member (seller with highest sales quantity)
     */
    Staff getTopSeller();

    /**
     * Filter products by expiration date (month and year) and optional search keyword
     */
    List<Product> filterExpiredProducts(Integer expMonth, Integer expYear, String category, String search);

    default List<Product> filterExpiredProducts(Integer expMonth, Integer expYear, String category) {
        return filterExpiredProducts(expMonth, expYear, category, null);
    }

    /**
     * Get top sales ranked with optional search filtering (category, product, buyer user)
     */
    List<CategorySalesDTO> getTopSalesRanked(String search);

    default List<CategorySalesDTO> getTopSalesRanked() {
        return getTopSalesRanked(null);
    }
}

