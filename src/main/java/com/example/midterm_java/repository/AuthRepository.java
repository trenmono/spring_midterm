package com.example.midterm_java.repository;

import com.example.midterm_java.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Staff, Integer> {

    Optional<Staff> findByUserName(String userName);

    boolean existsByUserName(String userName);

}
