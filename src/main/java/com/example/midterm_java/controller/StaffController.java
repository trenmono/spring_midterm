package com.example.midterm_java.controller;


import com.example.midterm_java.model.Role;
import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.RoleRepository;
import com.example.midterm_java.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;
    private RoleRepository roleRepository;

    @GetMapping
    public List<Staff> getAllStaff(@RequestParam(required = false) String name, @RequestParam(required = false) String search) {
        String query = (search != null && !search.trim().isEmpty()) ? search : name;
        if (query != null && !query.trim().isEmpty()) {
            return getStaffByName(query);
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

    @GetMapping("/search")
    public List<Staff> searchStaff(@RequestParam(required = false) String name, @RequestParam(required = false) String search) {
        String query = (search != null && !search.trim().isEmpty()) ? search : name;
        if (query != null && !query.trim().isEmpty()) {
            return getStaffByName(query);
        }
        return staffRepository.findAll();
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
            if (staff.getUserName() != null) {
                existing.setUserName(staff.getUserName());
            }
            if (staff.getPassword() != null) {
                existing.setPassword(staff.getPassword());
            }
            if (staff.getRole() != null) {
                Role roleInput = staff.getRole();
                if (roleInput.getRoleName() != null && !roleInput.getRoleName().trim().isEmpty()) {
                    String roleName = roleInput.getRoleName().trim().toUpperCase();
                    Role resolvedRole = staffRepository.findById(id).map(Staff::getRole).orElse(null);
                    Role roleEntity = roleRepository.findByRoleNameIgnoreCase(roleName)
                            .orElseGet(() -> roleRepository.save(new Role(roleName)));
                    existing.setRole(roleEntity);
                } else if (roleInput.getRId() > 0) {
                    roleRepository.findById(roleInput.getRId()).ifPresent(existing::setRole);
                }
            }

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
