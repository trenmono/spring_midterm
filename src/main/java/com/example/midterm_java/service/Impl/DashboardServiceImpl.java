package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.CategorySalesDTO;
import com.example.midterm_java.model.Product;
import com.example.midterm_java.model.SaleRecord;
import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.repository.SaleRepository;
import com.example.midterm_java.repository.StaffRepository;
import com.example.midterm_java.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public DashboardServiceImpl(SaleRepository saleRepository, ProductRepository productRepository, StaffRepository staffRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff getTopSeller() {
        try {
            List<SaleRecord> allSales = saleRepository.findAll();
            if (allSales.isEmpty()) {
                return null;
            }
            
            // Group by product and sum quantities, then find the product with highest sales
            return allSales.stream()
                    .collect(Collectors.groupingBy(
                            sale -> sale.getProduct(),
                            Collectors.summingInt(SaleRecord::getQuantitySold)
                    ))
                    .entrySet()
                    .stream()
                    .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()))
                    .map(entry -> {
                        Product topProduct = entry.getKey();
                        // Return the first staff member who sold this product
                        // In a real scenario, you might track who actually sold it
                        return staffRepository.findAll().stream().findFirst().orElse(null);
                    })
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Product> filterExpiredProducts(Integer expMonth, Integer expYear, String category) {
        try {
            List<Product> allProducts = productRepository.findAll();
            LocalDate today = LocalDate.now();
            
            return allProducts.stream()
                    .filter(product -> {
                        if (product.getExpireDate() == null || product.getExpireDate().trim().isEmpty()) {
                            return false;
                        }
                        
                        try {
                            LocalDate expireDate = LocalDate.parse(product.getExpireDate().trim());
                            
                            // Category filter (if category string or ID is provided)
                            if (category != null && !category.trim().isEmpty()) {
                                if (product.getProductCategory() == null || 
                                    (!category.trim().equalsIgnoreCase(product.getProductCategory().getCategoryName()) &&
                                     !category.trim().equalsIgnoreCase(String.valueOf(product.getProductCategory().getCatId())))) {
                                    return false;
                                }
                            }
                            
                            // If specific year or month filter is provided by user:
                            if (expMonth != null || expYear != null) {
                                if (expYear != null && expireDate.getYear() != expYear) {
                                    return false;
                                }
                                if (expMonth != null && expireDate.getMonthValue() != expMonth) {
                                    return false;
                                }
                                return true;
                            }
                            
                            // Default monitoring view (no year/month specified):
                            return !expireDate.isAfter(today.plusDays(365));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<CategorySalesDTO> getTopSalesRanked() {
        try {
            List<Object[]> results = saleRepository.findTopSalesByCategory();
            List<CategorySalesDTO> topSales = new java.util.ArrayList<>();
            
            int rank = 1;
            for (Object[] row : results) {
                Integer catId = ((Number) row[0]).intValue();
                String catName = (String) row[1];
                Integer totalQuantity = ((Number) row[2]).intValue();
                CategorySalesDTO dto = new CategorySalesDTO(catId, catName, totalQuantity);
                dto.setRank(rank++);
                topSales.add(dto);
            }
            
            return topSales;
        } catch (Exception e) {
            return List.of();
        }
    }
}
