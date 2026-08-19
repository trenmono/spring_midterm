package com.example.midterm_java.controller;

import com.example.midterm_java.model.Role;
import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.RoleRepository;
import com.example.midterm_java.repository.StaffRepository;
import com.example.midterm_java.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles authentication and top-level navigation only:
 * landing redirect, login, registration, and logout.
 * Dashboard pages live in {@link AdminDashboardController} and {@link StaffDashboardController}.
 */
@Controller
//@RequiredArgsConstructor
public class PageController {

    private final AuthService authService;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;

    public PageController(AuthService authService, StaffRepository staffRepository, RoleRepository roleRepository) {
        this.authService = authService;
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Staff staff = authService.login(username, password);
        if (staff != null) {
            session.setAttribute("user", staff);
            return redirectForRole(staff.getRoleName());
        }
        redirectAttributes.addFlashAttribute("toastMessage", "Invalid username or password");
        redirectAttributes.addFlashAttribute("toastType", "danger");
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {
        if (staffRepository.existsByUserName(username)) {
            redirectAttributes.addFlashAttribute("toastMessage", "Username already exists!");
            redirectAttributes.addFlashAttribute("toastType", "danger");
            return "redirect:/login";
        }

        Role staffRole = roleRepository.findByRoleName("STAFF")
                .orElseGet(() -> roleRepository.save(new Role("STAFF")));

        Staff newStaff = new Staff();
        newStaff.setUserName(username);
        newStaff.setPassword(password);
        newStaff.setRole(staffRole);
        staffRepository.save(newStaff);

        redirectAttributes.addFlashAttribute("toastMessage", "Registration successful! Please login.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String redirectForRole(String roleName) {
        if ("ADMIN".equalsIgnoreCase(roleName)) {
            return "redirect:/admin/dashboard";
        }
        if ("STOCK".equalsIgnoreCase(roleName) || "STAFF".equalsIgnoreCase(roleName)) {
            return "redirect:/staff/dashboard";
        }
        return "redirect:/store";
    }
}
