package com.example.midterm_java.controller;

import com.example.midterm_java.model.Category;
import com.example.midterm_java.model.Role;
import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.CategoryRepository;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.repository.RoleRepository;
import com.example.midterm_java.repository.StaffRepository;
import com.example.midterm_java.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;


@Controller
@RequestMapping("/admin")
//@RequiredArgsConstructor
public class AdminDashboardController {

    private final StaffRepository staffRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final DashboardService dashboardService;

    public AdminDashboardController(
            StaffRepository staffRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            RoleRepository roleRepository,
            DashboardService dashboardService
    ) {
        this.staffRepository = staffRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(defaultValue = "users") String module,
            @RequestParam(defaultValue = "list") String action,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer filterCategory,
            @RequestParam(required = false) Integer filterCategoryId,
            @RequestParam(required = false) Integer expMonth,
            @RequestParam(required = false) Integer expYear,
            @RequestParam(required = false) String search,
            Model model
    ) {
        model.addAttribute("module", module);
        model.addAttribute("action", action);
        model.addAttribute("search", search);
        model.addAttribute("topSeller", dashboardService.getTopSeller());

        switch (module.toLowerCase()) {
            case "users" -> loadUsers(model, action, id, search);
            case "products" -> loadProducts(model, action, id, filterCategory, search);
            case "categories" -> loadCategories(model, action, id, filterCategory, search);
            case "expired" -> loadExpired(model, filterCategory, expMonth, expYear, search);
            case "top-sales" -> loadTopSales(model, search, filterCategoryId);
            default -> loadUsers(model, action, id, search);
        }
        return "admin/dashboard";
    }

    private void loadUsers(Model model, String action, Integer id, String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("users", staffRepository.findByUserNameContainingIgnoreCase(search.trim()));
        } else {
            model.addAttribute("users", staffRepository.findAll());
        }
        if ("edit".equalsIgnoreCase(action) && id != null) {
            model.addAttribute("editUser", staffRepository.findById(id).orElse(null));
        }
    }

    private void loadProducts(Model model, String action, Integer id, Integer filterCategory, String search) {
        model.addAttribute("categories", categoryRepository.findAllByOrderByCatIdAsc());
        model.addAttribute("filterCategory", filterCategory);

        if (filterCategory != null) {
            Category cat = categoryRepository.findById(filterCategory).orElse(null);
            if (cat != null) {
                if (search != null && !search.trim().isEmpty()) {
                    model.addAttribute("products", productRepository.findByPNameContainingIgnoreCaseAndCategory(search.trim(), cat));
                } else {
                    model.addAttribute("products", productRepository.findByCategory(cat));
                }
            } else {
                model.addAttribute("products", Collections.emptyList());
            }
        } else if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("products", productRepository.findByPNameContainingIgnoreCase(search.trim()));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }

        if ("edit".equalsIgnoreCase(action) && id != null) {
            model.addAttribute("editProduct", productRepository.findById(id).orElse(null));
        }
    }

    private void loadCategories(Model model, String action, Integer id, Integer filterCategory, String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("categories", categoryRepository.findByCategoryNameContainingIgnoreCase(search.trim()));
        } else {
            model.addAttribute("categories", categoryRepository.findAllByOrderByCatIdAsc());
        }

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

    private void loadExpired(Model model, Integer filterCategory, Integer expMonth, Integer expYear, String search) {
        model.addAttribute("filterCategory", filterCategory);
        model.addAttribute("expMonth", expMonth);
        model.addAttribute("expYear", expYear);
        
        String categoryName = null;
        if (filterCategory != null) {
            Category cat = categoryRepository.findById(filterCategory).orElse(null);
            if (cat != null) {
                categoryName = cat.getCategoryName();
            }
        }
        
        model.addAttribute("expiredProducts", dashboardService.filterExpiredProducts(expMonth, expYear, categoryName, search));
    }

    private void loadTopSales(Model model, String search, Integer filterCategoryId) {
        // Full per-row ranked list (optionally filtered by search)
        java.util.List<com.example.midterm_java.model.CategorySalesDTO> ranked = dashboardService.getTopSalesRanked(search);

        // Apply category filter on top of search
        if (filterCategoryId != null) {
            ranked = ranked.stream()
                    .filter(dto -> dto.getCategoryId() == filterCategoryId)
                    .collect(java.util.stream.Collectors.toList());
            // Re-rank after filtering
            int rank = 1;
            for (com.example.midterm_java.model.CategorySalesDTO dto : ranked) {
                dto.setRank(rank++);
            }
        }

        model.addAttribute("topSales", ranked);
        // Aggregated per-category totals for pie chart
        model.addAttribute("categorySales", dashboardService.getSalesByCategory());
        model.addAttribute("filterCategoryId", filterCategoryId);
    }


    @PostMapping("/users/save")
    public String saveUser(
            @RequestParam(required = false) Integer id,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String role,
            RedirectAttributes redirectAttributes
    ) {
        if (id == null && staffRepository.existsByUserName(username)) {
            redirectAttributes.addFlashAttribute("toastMessage", "Username already exists!");
            redirectAttributes.addFlashAttribute("toastType", "danger");
            return "redirect:/admin/dashboard?module=users&action=add";
        }

        String targetRole = (role != null && !role.trim().isEmpty()) ? role.trim().toUpperCase() : "STAFF";

        Role staffRole = roleRepository.findByRoleNameIgnoreCase(targetRole)
                .orElseGet(() -> roleRepository.findByRoleName(targetRole)
                .orElseGet(() -> roleRepository.save(new Role(targetRole))));

        Staff staff = (id != null) ? staffRepository.findById(id).orElse(new Staff()) : new Staff();
        staff.setUserName(username);
        staff.setPassword(password);
        staff.setRole(staffRole);
        staffRepository.save(staff);

        redirectAttributes.addFlashAttribute("toastMessage", "User saved successfully!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/admin/dashboard?module=users";
    }


    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        staffRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "User deleted!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/admin/dashboard?module=users";
    }
}
