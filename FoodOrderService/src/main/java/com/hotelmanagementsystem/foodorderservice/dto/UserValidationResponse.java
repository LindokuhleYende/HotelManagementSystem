package com.hotelmanagementsystem.foodorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserValidationResponse {
    private boolean valid;
    private String role;
}
