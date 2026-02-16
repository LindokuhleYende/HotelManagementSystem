package com.hotelmanagementsystem.foodorderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserValidationResponse {

    private boolean valid;
    private String username;
    private String role;
    private String message;
}
