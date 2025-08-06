package com.example.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ApiResponse {

    private boolean success;
    private String message;
    private int status;
    private Object data;  // to hold any extra response data

    // No-args constructor
    public ApiResponse() {}

    // All-args constructor
    public ApiResponse(boolean success, String message, int status, Object data) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
