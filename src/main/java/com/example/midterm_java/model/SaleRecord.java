package com.example.midterm_java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Data
@Table(name = "sale_record")
public class SaleRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_name")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Staff user;

    @Column(name = "user_buy")
    private String userBuy;

    @Column(name = "quantity_sold")
    private int quantitySold;

    @Column(name = "sale_date")
    private LocalDate saleDate;

    public SaleRecord() {
    }

    public SaleRecord(Product product, int quantitySold, LocalDate saleDate) {
        this.product = product;
        this.productName = (product != null) ? product.getPName() : "";
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.userBuy = "";
    }

    public SaleRecord(Product product, int quantitySold, LocalDate saleDate, Staff user) {
        this.product = product;
        this.productName = (product != null) ? product.getPName() : "";
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.user = user;
        this.userBuy = (user != null) ? user.getUserName() : "";
    }

    public SaleRecord(Product product, int quantitySold, LocalDate saleDate, String userBuy) {
        this.product = product;
        this.productName = (product != null) ? product.getPName() : "";
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.userBuy = userBuy;
    }

    public SaleRecord(Product product, int quantitySold, LocalDate saleDate, Staff user, String userBuy) {
        this.product = product;
        this.productName = (product != null) ? product.getPName() : "";
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.user = user;
        this.userBuy = (userBuy != null && !userBuy.trim().isEmpty()) ? userBuy : (user != null ? user.getUserName() : "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null && (this.productName == null || this.productName.trim().isEmpty())) {
            this.productName = product.getPName();
        }
    }

    public String getProductName() {
        if (productName != null && !productName.trim().isEmpty()) {
            return productName;
        }
        return (product != null) ? product.getPName() : "";
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Staff getUser() {
        return user;
    }

    public void setUser(Staff user) {
        this.user = user;
        if (user != null && (this.userBuy == null || this.userBuy.trim().isEmpty())) {
            this.userBuy = user.getUserName();
        }
    }

    public String getUserBuy() {
        if (userBuy != null && !userBuy.trim().isEmpty()) {
            return userBuy;
        }
        return (user != null) ? user.getUserName() : "";
    }


    public void setUserBuy(String userBuy) {
        this.userBuy = userBuy;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}


