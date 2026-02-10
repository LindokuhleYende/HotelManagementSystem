package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.repository.FoodOrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final FoodOrderRepository foodOrderRepository;

    @PostMapping
    public ResponseEntity<FoodOrder> createOrder(
            @RequestBody FoodOrder order,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("X-User-Id");
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized if no user info
        }

        order.setCustomerUsername(username);
        order.setStatus(FoodOrder.Status.PENDING);

        FoodOrder savedOrder = foodOrderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    public ResponseEntity<List<FoodOrder>> getAllOrders(HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("STAFF"))) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        List<FoodOrder> orders = foodOrderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long id,
            HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || !role.equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        if (!foodOrderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        foodOrderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
