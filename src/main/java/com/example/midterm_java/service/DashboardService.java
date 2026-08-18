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
     * Filter products by expiration date (month and year)
     * @param expMonth the expiration month (1-12), or null for all months
     * @param expYear the expiration year, or null for all years
     * @param category optional category filter
     * @return list of expired or expiring products
     */
    List<Product> filterExpiredProducts(Integer expMonth, Integer expYear, String category);

    /**
     * Get top sales ranked by category
     */
    List<CategorySalesDTO> getTopSalesRanked();
}
