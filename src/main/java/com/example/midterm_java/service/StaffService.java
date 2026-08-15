package com.example.midterm_java.service;

import com.example.midterm_java.model.ApiResponse;
import com.example.midterm_java.model.Staff;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StaffService {

    ResponseEntity<ApiResponse<Staff>> addStaff(Staff staff);

    ResponseEntity<ApiResponse<Staff>> updateStaff(Staff staff);

    ResponseEntity<ApiResponse<Void>> deleteStaff(Staff staff);

    ResponseEntity<List<Staff>> findAllStaff();

    ResponseEntity<ApiResponse<Staff>> findByStaffId(Integer id);

    ResponseEntity<ApiResponse<List<Staff>>> findStaffByName(String name);



}
