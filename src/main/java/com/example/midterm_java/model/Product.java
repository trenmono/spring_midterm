package com.example.midterm_java.model;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pId;

    @Column(name="cls_productName")
    private String pName;

    @Column(name = "cls_quantity")
    private String qty;

    @Column(name = "cls_price")
    private double price;

    private String expireDate;

    @ManyToOne
    @JoinColumn(name = "cat_ID")
    private Category category;

    public Product() {
    }

    public Product(int pId, String pName, String qty, double price, String expireDate, Category category) {
        this.pId = pId;
        this.pName = pName;
        this.qty = qty;
        this.price = price;
        this.expireDate = expireDate;
        this.category = category;
    }

    public Integer getId() {
        return pId;
    }

    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getProductCategory() {
        return category;
    }

    public void setProductCategory(Category productCategory) {
        this.category = productCategory;
    }
}
