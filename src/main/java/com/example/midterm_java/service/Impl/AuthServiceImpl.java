package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.StaffRepository;
import com.example.midterm_java.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final StaffRepository staffRepository;

    @Autowired
    public AuthServiceImpl(StaffRepository staffRepository){
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff login(String userName, String password) {
        Optional<Staff> staffOpt = staffRepository.findByUserName(userName);
        if (staffOpt.isPresent()) {
            Staff user = staffOpt.get();
            if (user.getPassword() != null && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Optional<Staff> findByUsername(String username) {
        return staffRepository.findByUserName(username);
    }

    @Override
    public Staff logout(){
        return null;
    }

}
