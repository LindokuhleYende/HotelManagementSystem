package com.hotelmanagementsystem.roombookingservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String username; // From JWT

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime bookingTime;

    private Integer numberOfGuests;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String specialRequests;


}

