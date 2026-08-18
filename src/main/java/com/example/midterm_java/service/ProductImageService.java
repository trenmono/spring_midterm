package com.example.midterm_java.service;

import com.example.midterm_java.model.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    /**
     * Resolve the image name for a product.
     * If imageFile is provided, upload it and return the new name.
     * Otherwise, use the provided imageName or fall back to oldImageName.
     * 
     * @param imageFile the new image file (optional)
     * @param imageName the provided image name (optional)
     * @param oldImageName the previous image name (optional)
     * @param productName the product name to use for file naming
     * @return the resolved image name or null
     */
    String resolveImageName(MultipartFile imageFile, String imageName, String oldImageName, String productName);

    /**
     * Save a product image
     */
    ProductImage saveProductImage(ProductImage productImage);

    /**
     * Find all images for a product
     */
    List<ProductImage> findImagesByProductId(Integer productId);

    /**
     * Delete a product image by ID
     */
    void deleteProductImage(Integer imageId);

    /**
     * Upload image file to storage
     */
    String uploadImage(MultipartFile file, String productName) throws Exception;
}
