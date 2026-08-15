package com.example.midterm_java.repository;

import com.example.midterm_java.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    boolean existsByUserName(String name);

    @Query("SELECT s FROM Staff s WHERE s.sId = :id")
    Optional<Staff> findBySId(@Param("id") Integer id);

    Optional<Staff> findByUserName(String userName);

    List<Staff> findByUserNameContaining(String name);

    @Query("SELECT s FROM Staff s WHERE LOWER(s.userName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Staff> findByUserNameContainingIgnoreCase(@Param("name") String name);
}
