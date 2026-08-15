package com.example.midterm_java.repository;

import com.example.midterm_java.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPName(String pName);

    List<Product> findByCategory();

    boolean existsByPName(String pName);

    Optional<Product> findById(Integer id);
}
