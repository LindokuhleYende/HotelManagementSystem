package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.repository.FoodOrderRepository;
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
        order.setStatus(FoodOrder.Status.PENDING);
        FoodOrder savedOrder = foodOrderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // --- READ ---
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Get all food orders", description = "Retrieves a list of all food orders. Only ADMIN and MODERATOR roles are authorized to access this endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders"),
            @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FoodOrder>> getAllOrders() {
        List<FoodOrder> orders = foodOrderRepository.findAll();
        return ResponseEntity.ok(orders);
    }
@PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN') or hasRole (Moderator)")
@Operation(summary = "Update a food order", description = "Updates a food order. Only ADMIN role and MODERATOR is authorized to perform this action. ")
@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Order successfully updated"),
        @ApiResponse(responseCode = "403", description = "Forbidden – only ADMIN and MODERATOR can update orders"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
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
    @Operation(summary = "Delete a food order", description = "Deletes a food order by its unique ID. Only ADMIN role is authorized to perform this action.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden – only ADMIN can delete orders"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!foodOrderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        foodOrderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
