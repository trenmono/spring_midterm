package com.example.midterm_java.controller;

import com.example.midterm_java.model.Staff;
import com.example.midterm_java.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Staff login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username,password);
    }
}
