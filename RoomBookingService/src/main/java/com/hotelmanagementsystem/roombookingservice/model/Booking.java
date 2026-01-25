package com.hotelmanagementsystem.roombookingservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;
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

    @Column(nullable = false, unique = true)
    private String booking_number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate check_in_date;

    @Column(nullable = false)
    private LocalDate check_out_date;

    @Column
    private String availability_status;

    @Column(nullable = false)
    private String num_of_guests;
}
