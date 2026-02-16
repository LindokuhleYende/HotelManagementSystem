package com.hotelmanagementsystem.foodorderservice.entity;

import com.hotelmanagementsystem.foodorderservice.entity.OrderItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;
    private String customerUsername;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "food_order_id")
    private List<OrderItemEntity> items = new ArrayList<>();

    public enum Status {
        PENDING,
        ACCEPTED,
        PREPARING,
        READY_FOR_PICKUP,
        PICKED_UP,
        ON_THE_WAY,
        DELIVERED,
        COMPLETED,
        CANCELLED
    }
}
