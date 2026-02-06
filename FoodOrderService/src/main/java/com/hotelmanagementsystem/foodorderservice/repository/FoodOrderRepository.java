package com.hotelmanagementsystem.foodorderservice.repository;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    // Find orders by the associated booking ID
    List<FoodOrder> findByBookingId(String bookingId);

    // Find orders by the username of the customer who placed them
    List<FoodOrder> findByCustomerUsername(String customerUsername);

    // Find orders by their current status (e.g., PENDING, COMPLETED)
    List<FoodOrder> findByStatus(FoodOrder.Status status);
}
