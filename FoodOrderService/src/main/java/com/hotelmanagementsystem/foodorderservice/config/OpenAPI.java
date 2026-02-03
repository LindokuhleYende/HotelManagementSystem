package com.hotelmanagementsystem.foodorderservice.config;
import io.swagger.v3.oas.models.info.Info;

public class OpenAPI {
    public io.swagger.v3.oas.models.OpenAPI foodOrderOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("Food Order Service API")
                        .description("API for managing food orders in the hotel system")
                        .version("1.0"));
    }

}




