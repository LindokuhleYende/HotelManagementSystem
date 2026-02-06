package com.hotelmanagementsystem.foodorderservice;

import com.hotelmanagementsystem.foodorderservice.client.AuthServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class FoodOrderServiceApplicationTests {

    // Mock the AuthService Feign client so the context can load
    @MockBean
    private AuthServiceClient authServiceClient;

    @Test
    void contextLoads() {
        // This test only checks that the Spring context starts
    }
}
