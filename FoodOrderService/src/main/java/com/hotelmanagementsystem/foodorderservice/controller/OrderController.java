package com.hotelmanagementsystem.foodorderservice.controller;



import com.hotelmanagementsystem.foodorderservice.model.FoodOrder;
import com.hotelmanagementsystem.foodorderservice.model.OrderItem;
import com.hotelmanagementsystem.foodorderservice.model.MenuItem;
import com.hotelmanagementsystem.foodorderservice.repository.FoodOrderRepository;
import com.hotelmanagementsystem.foodorderservice.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final FoodOrderRepository foodOrderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderController(FoodOrderRepository foodOrderRepository, MenuItemRepository menuItemRepository) {
        this.foodOrderRepository = foodOrderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @PostMapping
    public FoodOrder createOrder(@RequestBody FoodOrder order) {
        double totalPrice = 0;
        for (OrderItem item : order.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItem().getId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            item.setMenuItem(menuItem);
            totalPrice += menuItem.getPrice() * item.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        order.setStatus(FoodOrder.Status.PENDING);
        return foodOrderRepository.save(order);
    }

    @GetMapping("/{id}")
    public FoodOrder getOrderById(@PathVariable Long id) {
        return foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }


    @GetMapping("/booking/{bookingId}")
    public List<FoodOrder> getOrdersByBooking(@PathVariable String bookingId) {
        return foodOrderRepository.findByBookingId(bookingId);
    }
}
