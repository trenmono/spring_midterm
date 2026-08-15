package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.StaffRepository;
import com.example.midterm_java.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final StaffRepository staffRepository;

    public AuthServiceImpl(StaffRepository staffRepository){
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff login(String userName, String password) {
        List<Staff> staff = staffRepository.findByUserName(userName);

        if (staff.isEmpty()) {
            Staff user = new Staff();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Optional<Staff> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Staff logout(){
        return null;
    }

}
