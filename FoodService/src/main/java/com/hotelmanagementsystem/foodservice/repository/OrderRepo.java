package com.hotelmanagementsystem.foodservice.repository;

import com.hotelmanagementsystem.foodservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {

}
