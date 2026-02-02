package com.hotelmanagementsystem.roombookingservice.model;

import jakarta.persistence.*;
import lombok.*;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.type.SqlTypes;
//
//import java.util.List;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private Double pricePerNight;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private Integer capacity;
    private String description;
    private String amenities; // e.g., "WiFi,TV,AC,MiniBar"

    @Column(columnDefinition = "TEXT")
    private String images;

    //the code below is for jsonb which is only supported by postgres
    //Once we use postgres then we will uncomment
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb")
//    private List<String> images;
//
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb")
//    private List<String> amenities;




}



