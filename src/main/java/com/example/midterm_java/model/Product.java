package com.example.midterm_java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
//@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pId;

    @Column(name = "cls_productName")
    private String pName;

    @Column(name = "cls_quantity")
    private String qty;

    @Column(name = "cls_price")
    private double price;

    private String expireDate;

    @Column(name = "image_name")
    private String imageName;

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

    public void setId(Integer id) {
        this.pId = id != null ? id : 0;
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

    public String getName() {
        return pName;
    }

    public void setName(String name) {
        this.pName = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQuantity() {
        return qty;
    }

    public void setQuantity(String quantity) {
        this.qty = quantity;
    }

    public int getQuantityNum() {
        if (qty == null) return 0;
        try {
            return Integer.parseInt(qty.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getQtyNum() {
        return getQuantityNum();
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

    public String getExpiredDate() {
        return expireDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expireDate = expiredDate;
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

    public Integer getCategoryId() {
        return category != null ? category.getCatId() : null;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
