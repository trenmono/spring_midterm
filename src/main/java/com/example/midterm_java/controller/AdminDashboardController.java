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
            @RequestParam(required = false) Integer expMonth,
            @RequestParam(required = false) Integer expYear,
            Model model
    ) {
        model.addAttribute("module", module);
        model.addAttribute("action", action);
        model.addAttribute("topSeller", dashboardService.getTopSeller());

        switch (module.toLowerCase()) {
            case "users" -> loadUsers(model, action, id);
            case "products" -> loadProducts(model, action, id);
            case "categories" -> loadCategories(model, action, id, filterCategory);
            case "expired" -> loadExpired(model, expMonth, expYear);
            case "top-sales" -> loadTopSales(model);
            default -> loadUsers(model, action, id);
        }
        return "admin/dashboard";
    }

    private void loadUsers(Model model, String action, Integer id) {
        model.addAttribute("users", staffRepository.findAll());
        if ("edit".equalsIgnoreCase(action) && id != null) {
            model.addAttribute("editUser", staffRepository.findById(id).orElse(null));
        }
    }

    private void loadProducts(Model model, String action, Integer id) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAllByOrderByCatIdAsc());
        if ("edit".equalsIgnoreCase(action) && id != null) {
            model.addAttribute("editProduct", productRepository.findById(id).orElse(null));
        }
    }

    private void loadCategories(Model model, String action, Integer id, Integer filterCategory) {
        model.addAttribute("categories", categoryRepository.findAllByOrderByCatIdAsc());

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

    private void loadExpired(Model model, Integer expMonth, Integer expYear) {
        model.addAttribute("expMonth", expMonth);
        model.addAttribute("expYear", expYear);
        model.addAttribute("expiredProducts", dashboardService.filterExpiredProducts(expMonth, expYear, null));
    }

    private void loadTopSales(Model model) {
        model.addAttribute("topSales", dashboardService.getTopSalesRanked());
    }

    @PostMapping("/users/save")
    public String saveUser(
            @RequestParam(required = false) Integer id,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            RedirectAttributes redirectAttributes
    ) {
        if (id == null && staffRepository.existsByUserName(username)) {
            redirectAttributes.addFlashAttribute("toastMessage", "Username already exists!");
            redirectAttributes.addFlashAttribute("toastType", "danger");
            return "redirect:/admin/dashboard?module=users&action=add";
        }

        Role staffRole = roleRepository.findByRoleName(role)
                .orElseGet(() -> roleRepository.save(new Role(role.toUpperCase())));

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
