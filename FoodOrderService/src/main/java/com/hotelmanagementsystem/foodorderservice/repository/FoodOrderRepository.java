package com.hotelmanagementsystem.foodorderservice.repository;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    // Fixed: Parameter changed from String to Long to match your entity type
    List<FoodOrder> findByBookingId(Long bookingId);
}
