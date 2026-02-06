package com.hotelmanagementsystem.foodorderservice.client;


import com.hotelmanagementsystem.foodorderservice.dto.UserValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

// Use property from application.properties instead of hardcoding URL
@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthServiceClient {

    @GetMapping("/api/auth/validate")
    UserValidationResponse validateToken(
            @RequestHeader("Authorization") String authorization
    );
}
