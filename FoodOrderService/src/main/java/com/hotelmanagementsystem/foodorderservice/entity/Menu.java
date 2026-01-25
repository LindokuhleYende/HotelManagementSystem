package com.hotelmanagementsystem.foodorderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    public Double price;

    @Column
    private String category;

    @Column
    private String images;
}
