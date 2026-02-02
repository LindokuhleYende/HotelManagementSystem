package com.hotelmanagementsystem.roombookingservice.dto;
import lombok.Data;

@Data
public class BookingResponse {
    private Long id;
    private RoomResponse room;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String username;
    private String checkInDate;
    private String checkOutDate;
    private String bookingTime;
    private Integer numberOfGuests;
    private Double totalPrice;
    private String status;
    private String specialRequests;
}
