package com.example.midterm_java.model;

public class ApiResponse<D> {

    private boolean success;
    private int status;
    private String message;
    private D data;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, int status, String message, D data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <D> ApiResponse<D> success(int status, String message, D data) {
        return new ApiResponse<>(true, status, message, data);
    }

    public static <D> ApiResponse<D> error(int status, String message) {
        return new ApiResponse<>(false, status, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
