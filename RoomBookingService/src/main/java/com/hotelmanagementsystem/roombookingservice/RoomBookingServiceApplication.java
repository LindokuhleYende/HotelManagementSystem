package com.hotelmanagementsystem.roombookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.hotelmanagementsystem")
@EnableJpaRepositories("com.hotelmanagementsystem")
@EnableDiscoveryClient
@EnableFeignClients
public class RoomBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomBookingServiceApplication.class, args);
    }

}
