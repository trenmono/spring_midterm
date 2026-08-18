package com.example.midterm_java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategorySalesDTO {

    private int categoryId;
    private String categoryName;
    private String productName;
    private String userBuy;
    private long totalSold;
    private int rank;

    public CategorySalesDTO(int categoryId, String categoryName, long totalSold) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productName = "N/A";
        this.userBuy = "";
        this.totalSold = totalSold;
    }

    public CategorySalesDTO(int categoryId, String categoryName, String productName, String userBuy, long totalSold) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productName = (productName != null && !productName.trim().isEmpty()) ? productName : "N/A";
        this.userBuy = (userBuy != null) ? userBuy : "";
        this.totalSold = totalSold;
    }


    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getUserBuy() { return userBuy; }


    public void setUserBuy(String userBuy) { this.userBuy = userBuy; }

    public long getTotalSold() { return totalSold; }
    public void setTotalSold(long totalSold) { this.totalSold = totalSold; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
}

