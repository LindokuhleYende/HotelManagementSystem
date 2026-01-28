package com.hotelmanagementsystem.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long menu_id;

    @Column
    private String menu_item;

    @Column
    private Double total_price;

    @Column
    private String order_status; //pending, completed, cancelled

    @Column
    private String customization;

}
