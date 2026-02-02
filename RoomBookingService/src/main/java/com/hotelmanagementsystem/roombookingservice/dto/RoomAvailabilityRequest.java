package com.hotelmanagementsystem.roombookingservice.dto;
import lombok.Data;

@Data
public class RoomAvailabilityRequest {
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private Integer guests;
}
