package com.example.midterm_java.repository;

import com.example.midterm_java.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface productImageRepository extends JpaRepository<ProductImage, Integer> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.pId = :productId")
    List<ProductImage> findByProductId(@Param("productId") Integer productId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.imageName = :imageName")
    ProductImage findByImageName(@Param("imageName") String imageName);
}
