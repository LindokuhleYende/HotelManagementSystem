package com.hotelmanagementsystem.foodorderservice.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "food_order") // Explicitly names the table to match your Flyway script
public class FoodOrder {   // 👈 FIXED: Changed back to FoodOrder to match the file name and constructor types

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;

    @Column(columnDefinition = "NUMERIC(10,2)") // Preserves the exact decimal precision mapping
    private Double totalPrice;

    private String status = "PENDING";

    // Many-to-many relationship with MenuItem
    @ManyToMany
    @JoinTable(
            name = "food_order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> items;

    // Constructors
    public FoodOrder() {}  // 👈 FIXED: Now correctly matches the class type definition above

    public FoodOrder(Long bookingId, List<MenuItem> items, Double totalPrice) {
        this.bookingId = bookingId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = "PENDING";
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
