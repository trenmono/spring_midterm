package com.example.midterm_java.repository;

import com.example.midterm_java.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    List<Staff> findByUserName(String name);

    boolean existsByUserName(String name);

    Optional<Staff> findBySId(Integer id);


}
