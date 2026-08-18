package com.example.midterm_java.repository;

import com.example.midterm_java.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByOrderByCatIdAsc();

    List<Category> findByCategoryName(String name);

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByCategoryNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByCategoryName(String name);

    @Query("SELECT c FROM Category c WHERE c.catId = :catId")
    Optional<Category> findByCatId(@Param("catId") Integer catId);

}
