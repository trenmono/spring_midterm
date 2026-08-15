package com.example.midterm_java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @UpdateTimestamp
    private String expireDate;

    @ManyToOne
    @JoinColumn(name = "cat_ID")
    private Category category;

    public Integer getId() {
        return pId;
    }

    public Object getProductCategory() {
        return category;
    }

    public void setProductCategory(Object productCategory) {
        this.category = (Category) productCategory;
    }
}
