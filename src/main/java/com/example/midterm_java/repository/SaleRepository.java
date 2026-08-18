package com.example.midterm_java.repository;

import com.example.midterm_java.model.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<SaleRecord, Integer> {
    @Query("SELECT p.category.catId, p.category.categoryName, SUM(s.quantitySold) " +
           "FROM SaleRecord s JOIN s.product p " +
           "WHERE p.category IS NOT NULL " +
           "GROUP BY p.category.catId, p.category.categoryName " +
           "ORDER BY SUM(s.quantitySold) DESC")
    List<Object[]> findTopSalesByCategory();

    @Query("SELECT COALESCE(p.category.catId, 0), COALESCE(p.category.categoryName, 'Uncategorized'), p.pName, s.userBuy, SUM(s.quantitySold) " +
           "FROM SaleRecord s JOIN s.product p " +
           "GROUP BY p.category.catId, p.category.categoryName, p.pName, s.userBuy " +
           "ORDER BY SUM(s.quantitySold) DESC")
    List<Object[]> findTopSalesDetailed();
}



