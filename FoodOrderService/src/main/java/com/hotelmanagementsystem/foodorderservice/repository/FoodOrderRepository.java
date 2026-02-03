package com.hotelmanagementsystem.foodorderservice.repository;


import com.hotelmanagementsystem.foodorderservice.model.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByBookingId(String bookingId);
}
