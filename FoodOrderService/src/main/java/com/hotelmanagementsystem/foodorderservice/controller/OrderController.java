package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.repository.FoodOrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "FoodOrder API", description = "API for managing food orders")
public class OrderController {

    private final FoodOrderRepository foodOrderRepository;

    @PostMapping
    @Operation(
            summary = "Create a food order",
            description = "Creates a new food order for the authenticated user. The order is initialized with status PENDING."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user ID not provided"),
            @ApiResponse(responseCode = "400", description = "Bad request – invalid order details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FoodOrder> createOrder(@RequestBody FoodOrder order,
                                                 HttpServletRequest request) {

        String username = (String) request.getAttribute("X-User-Id");

        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        order.setCustomerUsername(username);
        order.setStatus(FoodOrder.Status.PENDING);

        FoodOrder savedOrder = foodOrderRepository.save(order);

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    @Operation(
            summary = "Get all food orders",
            description = "Retrieves a list of all food orders. Only ADMIN and MODERATOR roles are authorized to access this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders"),
            @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FoodOrder>> getAllOrders(HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");

        if (role == null || (!role.equals("ADMIN") && !role.equals("MODERATOR"))) {
            return ResponseEntity.status(403).build();
        }

        List<FoodOrder> orders = foodOrderRepository.findAll();

        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a food order",
            description = "Deletes a food order by its unique ID. Only ADMIN role is authorized to perform this action."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden – only ADMIN can delete orders"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id,
                                            HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        if (!foodOrderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        foodOrderRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
