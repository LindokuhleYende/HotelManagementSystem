package com.hotelmanagementsystem.foodorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//Eureka
@SpringBootApplication
@EnableFeignClients(basePackages = "com.hotelmanagementsystem.foodorderservice.client")
public class FoodOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodOrderServiceApplication.class, args);
    }
}
