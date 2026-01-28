package com.hotelmanagementsystem.foodservice.controllers;

//Order Controller will be used to create an order by guests
//Moderators will be able to view the order and update the order status

import com.hotelmanagementsystem.foodservice.entity.Order;
import com.hotelmanagementsystem.foodservice.repository.OrderRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/orders")
@Tag(name= "Order API", description = "This is an Api to create an Food orders, update and view them")
public class OrderController {
    private final OrderRepo orderRepo;

    public OrderController(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order){
        order.setId(null); // ensures a create
        Order savedOrder = orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder).getBody();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepo.findById(id).orElse(null);
    }

    @GetMapping
    public Iterable<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id,@RequestBody Order order){
        Optional<Order> existing = orderRepo.findById(id);
        if(existing.isEmpty()){
            return null;
        }
        order.setId(id);
        Order updatedOrder = orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder).getBody();
    }
}
