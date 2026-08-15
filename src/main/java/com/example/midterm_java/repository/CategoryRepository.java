package com.example.midterm_java.repository;

import com.example.midterm_java.model.Category;
import com.example.midterm_java.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findByCategoryName(String name);

    boolean existsByCategoryName(String name);

    Optional<Category> findByCategoryId(Integer id);


}
