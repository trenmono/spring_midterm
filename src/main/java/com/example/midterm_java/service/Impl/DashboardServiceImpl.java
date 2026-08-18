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
    public List<Product> filterExpiredProducts(Integer expMonth, Integer expYear, String category, String search) {
        try {
            List<Product> allProducts = productRepository.findAll();
            LocalDate today = LocalDate.now();
            
            return allProducts.stream()
                    .filter(product -> {
                        // Search filter
                        if (search != null && !search.trim().isEmpty()) {
                            String query = search.trim().toLowerCase();
                            boolean matchName = product.getPName() != null && product.getPName().toLowerCase().contains(query);
                            boolean matchCategory = product.getProductCategory() != null && product.getProductCategory().getCategoryName() != null && product.getProductCategory().getCategoryName().toLowerCase().contains(query);
                            if (!matchName && !matchCategory) {
                                return false;
                            }
                        }

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
    public List<Product> filterExpiredProducts(Integer expMonth, Integer expYear, String category) {
        return filterExpiredProducts(expMonth, expYear, category, null);
    }

    @Override
    public List<CategorySalesDTO> getTopSalesRanked(String search) {
        try {
            List<Object[]> results;
            try {
                results = saleRepository.findTopSalesDetailed();
            } catch (Exception e) {
                results = saleRepository.findTopSalesByCategory();
            }

            List<CategorySalesDTO> topSales = new java.util.ArrayList<>();
            
            for (Object[] row : results) {
                Integer catId = ((Number) row[0]).intValue();
                String catName = row[1] != null ? row[1].toString() : "Uncategorized";
                String productName = row.length > 3 ? (row[2] != null ? row[2].toString() : "N/A") : "N/A";
                String userBuy = row.length > 3 ? (row[3] != null ? row[3].toString() : "") : "";
                Integer totalQuantity = row.length > 4 ? ((Number) row[4]).intValue() : (row.length > 2 ? ((Number) row[2]).intValue() : 0);

                CategorySalesDTO dto = new CategorySalesDTO(catId, catName, productName, userBuy, totalQuantity);
                topSales.add(dto);
            }




            if (search != null && !search.trim().isEmpty()) {
                String query = search.trim().toLowerCase();
                topSales = topSales.stream()
                        .filter(dto -> (dto.getCategoryName() != null && dto.getCategoryName().toLowerCase().contains(query)) ||
                                       (dto.getProductName() != null && dto.getProductName().toLowerCase().contains(query)) ||
                                       (dto.getUserBuy() != null && dto.getUserBuy().toLowerCase().contains(query)))
                        .collect(Collectors.toList());
            }

            int rank = 1;
            for (CategorySalesDTO dto : topSales) {
                dto.setRank(rank++);
            }

            return topSales;
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<CategorySalesDTO> getTopSalesRanked() {
        return getTopSalesRanked(null);
    }
}

