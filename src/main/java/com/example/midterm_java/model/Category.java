package com.example.midterm_java.model;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int catId;

    @Column(unique=true, name="cls_categoryName")
    private String categoryName;

    public Category() {
    }

    public Category(int catId, String categoryName) {
        this.catId = catId;
        this.categoryName = categoryName;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
