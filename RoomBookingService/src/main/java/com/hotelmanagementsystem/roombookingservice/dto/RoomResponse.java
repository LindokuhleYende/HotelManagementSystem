package com.hotelmanagementsystem.roombookingservice.dto;

import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
    private String status;
    private Integer capacity;
    private String description;
    private String amenities;
    private String images;
}