package com.example.midterm_java.controller;

import com.example.midterm_java.model.Category;
import com.example.midterm_java.repository.CategoryRepository;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;


@Controller
@RequestMapping("/staff")
//@RequiredArgsConstructor
public class StaffDashboardController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DashboardService dashboardService;

    public StaffDashboardController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            DashboardService dashboardService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(defaultValue = "products") String module,
            @RequestParam(defaultValue = "list") String action,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer filterCategory,
            @RequestParam(required = false) Integer expMonth,
            @RequestParam(required = false) Integer expYear,
            Model model
    ) {
        model.addAttribute("module", module);
        model.addAttribute("action", action);
        model.addAttribute("categories", categoryRepository.findAllByOrderByCatIdAsc());
        model.addAttribute("topSeller", dashboardService.getTopSeller());

        switch (module.toLowerCase()) {
            case "categories" -> loadCategories(model, action, id, filterCategory);
            case "expired" -> loadExpired(model, filterCategory, expMonth, expYear);
            case "top-sales" -> loadTopSales(model);
            default -> loadProducts(model, action, id, filterCategory);
        }

        return "staff/dashboard";
    }

    private void loadProducts(Model model, String action, Integer id, Integer filterCategory) {
        if (filterCategory != null) {
            Category cat = categoryRepository.findById(filterCategory).orElse(null);
            model.addAttribute("products", cat != null ? productRepository.findByCategory(cat) : Collections.emptyList());
            model.addAttribute("filterCategory", filterCategory);
        } else {
            model.addAttribute("products", productRepository.findAll());
        }

        if ("edit".equalsIgnoreCase(action) && id != null) {
            model.addAttribute("editProduct", productRepository.findById(id).orElse(null));
        }
    }

    private void loadCategories(Model model, String action, Integer id, Integer filterCategory) {
        if (filterCategory != null) {
            Category cat = categoryRepository.findById(filterCategory).orElse(null);
            model.addAttribute("categoryProducts", cat != null ? productRepository.findByCategory(cat) : Collections.emptyList());
            model.addAttribute("filterCategory", filterCategory);
        } else {
            model.addAttribute("categoryProducts", productRepository.findAll());
        }

        if ("edit".equalsIgnoreCase(action) && id != null) {
            model.addAttribute("editCategory", categoryRepository.findById(id).orElse(null));
        }
    }

    private void loadExpired(Model model, Integer filterCategory, Integer expMonth, Integer expYear) {
        model.addAttribute("filterCategory", filterCategory);
        model.addAttribute("expMonth", expMonth);
        model.addAttribute("expYear", expYear);
        
        // Convert filterCategory ID to category name string
        String categoryName = null;
        if (filterCategory != null) {
            Category cat = categoryRepository.findById(filterCategory).orElse(null);
            if (cat != null) {
                categoryName = cat.getCategoryName();
            }
        }
        
        model.addAttribute("expiredProducts", dashboardService.filterExpiredProducts(expMonth, expYear, categoryName));
    }

    private void loadTopSales(Model model) {
        model.addAttribute("topSales", dashboardService.getTopSalesRanked());
    }
}
