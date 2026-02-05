package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.model.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.model.OrderItem;
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

        for (OrderItem item : order.getItems()) {
            // Fetch Menu item from DB
            Menu menu = menuRepository.findById(item.getMenu().getId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            item.setMenu(menu);
            item.setSubtotal(menu.getPrice() * item.getQuantity());
            totalPrice += item.getSubtotal();
        }

        order.setTotalPrice(totalPrice);
        order.setStatus(FoodOrder.Status.PENDING);

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


    @GetMapping
    public List<FoodOrder> getAllOrders(){
        return foodOrderRepository.findAll();
}


    /**
     * Get all orders for a booking
     */
    @GetMapping("/booking/{bookingId}")
    public List<FoodOrder> getOrdersByBooking(@PathVariable String bookingId) {
        return foodOrderRepository.findByBookingId(bookingId);
    }

    /**
     * Update order status (MODERATOR / ADMIN)
     * Example:
     * PUT /orders/1/status?status=DELIVERED
     */
    @PutMapping("/{id}/status")
    public FoodOrder updateOrderStatus(
            @PathVariable Long id,
            @RequestParam FoodOrder.Status status
    ) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return foodOrderRepository.save(order);
    }
}
