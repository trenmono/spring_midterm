package com.example.midterm_java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ApiResponse<D> {

    private boolean success;
    private int status;
    private String message;
    private D data;
}
