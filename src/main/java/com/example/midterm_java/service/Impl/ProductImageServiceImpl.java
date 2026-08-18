package com.example.midterm_java.service.Impl;

import com.example.midterm_java.model.ProductImage;
import com.example.midterm_java.repository.productImageRepository;
import com.example.midterm_java.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final productImageRepository productImageRepository;

    @Value("${file.upload.path:src/main/resources/static/images/}")
    private String uploadPath;

    @Autowired
    public ProductImageServiceImpl(productImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public String resolveImageName(MultipartFile imageFile, String imageName, String oldImageName, String productName) {
        try {
            // If a new file is provided, upload it
            if (imageFile != null && !imageFile.isEmpty()) {
                return uploadImage(imageFile, productName);
            }

            // If imageName is provided, use it
            if (imageName != null && !imageName.isEmpty()) {
                return imageName;
            }

            // Fall back to old image name
            return oldImageName;
        } catch (Exception e) {
            // If upload fails, fall back to imageName or oldImageName
            return (imageName != null && !imageName.isEmpty()) ? imageName : oldImageName;
        }
    }

    @Override
    public String uploadImage(MultipartFile file, String productName) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Generate unique filename
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String fileName = productName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + 
                            UUID.randomUUID().toString() + "." + fileExtension;

            // Save the file
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return fileName;
        } catch (IOException e) {
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductImage saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    public List<ProductImage> findImagesByProductId(Integer productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public void deleteProductImage(Integer imageId) {
        productImageRepository.deleteById(imageId);
    }

    /**
     * Extract file extension from filename
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "jpg"; // default extension
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
