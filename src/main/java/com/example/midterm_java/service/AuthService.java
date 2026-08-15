package com.example.midterm_java.service;

import com.example.midterm_java.model.Staff;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthService {

    Staff login(String username, String password);

    Optional<Staff> findByUsername(String username);

    Staff logout();


}
