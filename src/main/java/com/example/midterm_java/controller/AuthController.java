package com.example.midterm_java.controller;

import com.example.midterm_java.model.Staff;
import com.example.midterm_java.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.midterm_java.repository.AuthRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;

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
