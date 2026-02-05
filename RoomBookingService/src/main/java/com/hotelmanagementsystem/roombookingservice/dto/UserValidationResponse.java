package com.hotelmanagementsystem.roombookingservice.dto;
import lombok.Data;

@Data
public class UserValidationResponse {
    private boolean valid;
    private String username;
    private String role;
    private String message;
}