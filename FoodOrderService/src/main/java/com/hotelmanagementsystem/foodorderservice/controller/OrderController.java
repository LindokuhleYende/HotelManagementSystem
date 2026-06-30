package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.entity.MenuItem;
import com.hotelmanagementsystem.foodorderservice.repository.FoodOrderRepository;
import com.hotelmanagementsystem.foodorderservice.repository.MenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "Endpoints for managing food orders")
public class OrderController {

    private final FoodOrderRepository foodOrderRepository;
    private final MenuRepository menuRepository;

    // --- CREATE ---
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Create a food order", description = "Creates a new food order for the authenticated user. The order is initialized with status PENDING.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user not authenticated"),
            @ApiResponse(responseCode = "400", description = "Bad request – invalid order details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FoodOrder> createOrder(@RequestBody FoodOrder order) {
        double totalPrice = 0;

        // Loop through MenuItems attached to the order
        for (MenuItem item : order.getItems()) {
            Menu dbMenu = menuRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            totalPrice += dbMenu.getPrice();
        }

        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");

        FoodOrder savedOrder = foodOrderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // --- READ ---
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves a food order by its unique ID.")
    public ResponseEntity<FoodOrder> getOrderById(@PathVariable Long id) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Get all food orders", description = "Retrieves a list of all food orders. Only ADMIN and MODERATOR roles are authorized.")
    public ResponseEntity<List<FoodOrder>> getAllOrders() {
        return ResponseEntity.ok(foodOrderRepository.findAll());
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get orders by booking ID", description = "Retrieves all food orders associated with a specific booking.")
    public ResponseEntity<List<FoodOrder>> getOrdersByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(foodOrderRepository.findByBookingId(bookingId));
    }

    // --- UPDATE ---
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates the status of a food order by its unique ID.")
    public ResponseEntity<FoodOrder> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return ResponseEntity.ok(foodOrderRepository.save(order));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Update a food order", description = "Updates a food order. Only ADMIN and MODERATOR roles are authorized.")
    public ResponseEntity<Void> updateOrder(@RequestBody FoodOrder order) {
        FoodOrder existingOrder = foodOrderRepository.findById(order.getId()).orElse(null);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }
        existingOrder.setStatus(order.getStatus());
        foodOrderRepository.save(existingOrder);
        return ResponseEntity.noContent().build();
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a food order", description = "Deletes a food order by its unique ID. Only ADMIN role is authorized.")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!foodOrderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        foodOrderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
