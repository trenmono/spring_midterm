package com.example.midterm_java.repository;

import com.example.midterm_java.model.Staff;

import java.util.List;
import java.util.Optional;

public interface AuthRepository {

    List<Staff> findByUserName(String userName);

    List<Staff> findPassword(String password);

    boolean existsByUserName(String userName);

    boolean existsByPassword(String password);

//    Optional<Staff> findByUserName(String userName);


}
