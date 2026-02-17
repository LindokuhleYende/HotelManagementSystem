package com.hotelmanagementsystem.foodorderservice.client;

import com.hotelmanagementsystem.foodorderservice.dto.UserValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthServiceClient {

    @GetMapping("/validate")
    UserValidationResponse validateUser(@RequestHeader("Authorization") String token);
}
