package com.hotelmanagementsystem.foodorderservice.repository;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByBookingId(String bookingId);

    List<FoodOrder> findByCustomerUsername(String customerUsername);

    List<FoodOrder> findByStatus(FoodOrder.Status status);
}
