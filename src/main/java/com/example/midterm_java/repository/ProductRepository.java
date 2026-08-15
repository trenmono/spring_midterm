package com.example.midterm_java.repository;

import com.example.midterm_java.model.Category;
import com.example.midterm_java.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.pName = :pName")
    List<Product> findByPName(@Param("pName") String pName);

    @Query("SELECT p FROM Product p WHERE LOWER(p.pName) LIKE LOWER(CONCAT('%', :pName, '%'))")
    List<Product> findByPNameContainingIgnoreCase(@Param("pName") String pName);

    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> findByCategory(@Param("category") Category category);

    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName")
    List<Product> findByCategoryCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.pName = :pName")
    boolean existsByPName(@Param("pName") String pName);

    @Query("SELECT p FROM Product p WHERE p.pId = :id")
    Optional<Product> findById(@Param("id") Integer id);
}
