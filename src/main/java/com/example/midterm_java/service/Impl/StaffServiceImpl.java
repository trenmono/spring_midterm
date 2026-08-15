package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.ApiResponse;
import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.StaffRepository;
import com.example.midterm_java.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<Staff>> addStaff(Staff staff) {
        if(staff == null || staff.getUserName() == null || staff.getUserName().trim().isEmpty() ||
           staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            400,
                            "Invalid username or password",
                            null
                    ));
        }
        boolean checkStaff = staffRepository.existsByUserName(staff.getUserName());
        if(checkStaff) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(
                            false,
                            409,
                            "User name already exists",
                            null
                    ));
        }
        Staff savedStaff = staffRepository.save(staff);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        201,
                        "Staff Created Successfully",
                        savedStaff
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<Staff>> updateStaff(Staff staff) {
        Optional<Staff> existingStaff = staffRepository.findBySId(staff.getSId());
        if(existingStaff.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Staff not Found",
                            null
                    ));
        }
        Staff updatedStaff = existingStaff.get();
        updatedStaff.setUserName(staff.getUserName());
        updatedStaff.setPassword(staff.getPassword());
        updatedStaff.setRole(staff.getRole());
        Staff savedStaff = staffRepository.save(updatedStaff);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Staff updated successfully",
                        savedStaff
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteStaff(Staff staff) {
        Optional<Staff> existingStaff = staffRepository.findBySId(staff.getSId());
        if(existingStaff.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Staff not Found",
                            null
                    ));
        }
        staffRepository.delete(existingStaff.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        200,
                        "Staff deleted successfully",
                        null
                ));
    }

    @Override
    public ResponseEntity<List<Staff>> findAllStaff() {
        List<Staff> staff = staffRepository.findAll();
        if(staff.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(staff);
        }
        return ResponseEntity.ok(staff);
    }

    @Override
    public ResponseEntity<ApiResponse<Staff>> findByStaffId(Integer id) {
        Optional<Staff> staff = staffRepository.findBySId(id);
        if (staff.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Staff not found",
                            null
                    ));
        }
        return ResponseEntity.ok(new ApiResponse<>(
                        true,
                        200,
                        "Staff found",
                        staff.get()
                ));
    }

    @Override
    public ResponseEntity<ApiResponse<List<Staff>>> findStaffByName(String name) {
        List<Staff> staff = staffRepository.findByUserNameContaining(name);
        if(staff.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            404,
                            "Staff not found",
                            null
                    ));
        }
        return ResponseEntity.ok(new ApiResponse<>(
                        true,
                        200,
                        "Staff Found",
                        staff
                ));
    }
}
