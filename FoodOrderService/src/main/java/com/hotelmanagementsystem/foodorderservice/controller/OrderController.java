package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.entity.MenuItem;
import com.hotelmanagementsystem.foodorderservice.repository.FoodOrderRepository;
import com.hotelmanagementsystem.foodorderservice.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final FoodOrderRepository foodOrderRepository;
    private final MenuItemRepository menuRepository;

    public OrderController(FoodOrderRepository foodOrderRepository,
                           MenuItemRepository menuRepository) {
        this.foodOrderRepository = foodOrderRepository;
        this.menuRepository = menuRepository;
    }

    /**
     * Create a new food order
     */
    @PostMapping
    public FoodOrder createOrder(@RequestBody FoodOrder order) {
        double totalPrice = 0;

        // Loop through the list of MenuItems directly attached to the order
        for (MenuItem item : order.getItems()) {
            // FIXED: Assigned to MenuItem type instead of Menu type
            Menu menuItem = menuRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            totalPrice += menuItem.getPrice();
        }

        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");

        return foodOrderRepository.save(order);
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public FoodOrder getOrderById(@PathVariable Long id) {
        return foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Get all orders
     */
    @GetMapping
    public List<FoodOrder> getAllOrders(){
        return foodOrderRepository.findAll();
    }

    /**
     * Get all orders for a booking
     */
    @GetMapping("/booking/{bookingId}")
    public List<FoodOrder> getOrdersByBooking(@PathVariable Long bookingId) {
        // FIXED: Dropped String.valueOf() to pass the raw Long ID cleanly
        return foodOrderRepository.findByBookingId(bookingId);
    }

    /**
     * Update order status
     */
    @PutMapping("/{id}/status")
    public FoodOrder updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return foodOrderRepository.save(order);
    }
}
