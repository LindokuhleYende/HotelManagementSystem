package com.hotelmanagementsystem.foodorderservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private double price;

    // Optional back-reference
    // @ManyToOne
    // private FoodOrder foodOrder;

    // Constructors, getters, setters
    public OrderItemEntity() {}

    public OrderItemEntity(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // getters and setters...
}
