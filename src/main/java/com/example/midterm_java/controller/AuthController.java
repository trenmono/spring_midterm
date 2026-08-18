package com.example.midterm_java.controller;

import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.AuthRepository;
import com.example.midterm_java.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthRepository authRepository;

    public AuthController(AuthService authService, AuthRepository authRepository) {
        this.authService = authService;
        this.authRepository = authRepository;
    }

    @PostMapping("/login")
    public Staff login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username,password);
    }

    @GetMapping("/name/{name}")
    public Staff getByName(@PathVariable String name) {
        return authRepository.findByUserName(name).orElse(null);
    }

    @GetMapping("/search")
    public Staff searchByName(@RequestParam String name) {
        return authRepository.findByUserName(name).orElse(null);
    }
}
