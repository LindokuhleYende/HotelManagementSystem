package com.hotelmanagementsystem.foodorderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;       // <-- add this
    private String category;          // <-- add this
    private boolean available;        // <-- add this
    private BigDecimal price;

    @ElementCollection
    private List<String> images;      // <-- add this if you want multiple images
}
