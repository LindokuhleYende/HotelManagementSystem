package com.hotelmanagementsystem.roombookingservice.dto;
import lombok.Data;

@Data
public class BookingRequest {
    private Long roomId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String checkInDate;
    private String checkOutDate;
    private Integer numberOfGuests;
    private String specialRequests;
}
