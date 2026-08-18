package com.example.midterm_java.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException productNotFound() {
        return new ResourceNotFoundException("Product not found");
    }

    public static ResourceNotFoundException categoryNotFound() {
        return new ResourceNotFoundException("Category not found");
    }

    public static ResourceNotFoundException staffNotFound() {
        return new ResourceNotFoundException("Staff not found");
    }
}
