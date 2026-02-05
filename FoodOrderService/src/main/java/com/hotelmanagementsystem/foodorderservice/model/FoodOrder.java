package com.hotelmanagementsystem.foodorderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "food_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate orderDate = LocalDate.now();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_order_id") // foreign key in OrderItem
    private List<OrderItem> items;

    public enum Status {
        PENDING, CONFIRMED, PREPARING, DELIVERED, COMPLETED, CANCELLED
    }
}
