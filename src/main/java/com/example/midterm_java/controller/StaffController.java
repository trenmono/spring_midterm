package com.example.midterm_java.controller;


import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;

    @GetMapping
    public List<Staff> getAllStaff(@RequestParam(required = false) String name) {
        if (name != null && !name.trim().isEmpty()) {
            return getStaffByName(name);
        }
        return staffRepository.findAll();
    }

    @GetMapping("/{id}")
    public Staff getStaffById(@PathVariable Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @GetMapping("/name/{name}")
    public List<Staff> getStaffByName(@PathVariable String name) {
        List<Staff> list = staffRepository.findByUserNameContainingIgnoreCase(name);
        if (list.isEmpty()) {
            staffRepository.findByUserName(name).ifPresent(list::add);
        }
        return list;
    }

    @GetMapping("/search/{name}")
    public List<Staff> getStaffByNamePath(@PathVariable String name) {
        return getStaffByName(name);
    }

    @PostMapping
    public Staff createStaff(@RequestBody Staff staff) {
        return staffRepository.save(staff);
    }

    @PutMapping("/update/{id}")
    public Staff updateStaff(
            @PathVariable Integer id,
            @RequestBody Staff staff
    ) {
        Staff existing = staffRepository.findById(id).orElse(null);

        if (existing != null) {
            existing.setUserName(staff.getUserName());
            existing.setPassword(staff.getPassword());

            return staffRepository.save(existing);
        }

        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Integer id) {
        staffRepository.deleteById(id);
        return "Staff deleted successfully";
    }
}
