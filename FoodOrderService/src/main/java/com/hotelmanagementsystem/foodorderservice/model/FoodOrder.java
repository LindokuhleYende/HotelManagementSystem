package com.hotelmanagementsystem.foodorderservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FoodOrder {

    private Long id;  // No @Id, just a plain field

    private String customerUsername;

    private Status status;

    private List<OrderItem> items = new ArrayList<>();

    public enum Status {
        PENDING,
        PREPARING,
        DELIVERED,
        COMPLETED,
        CANCELLED
    }
}
