package com.hotelmanagementsystem.roombookingservice.dto;
import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
    private Integer capacity;
    private String description;
    private String amenities;
    private String images;
}

