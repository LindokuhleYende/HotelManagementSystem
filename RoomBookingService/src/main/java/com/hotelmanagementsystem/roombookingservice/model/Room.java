package com.hotelmanagementsystem.roombookingservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String room_name;

    @Column(nullable = false)
    private String room_number;

    @Column(nullable = false)
    private Double price_per_night;

    @Column(nullable = false)
    private String room_type;

    @Column(nullable = false)
    private String max_num_guests;

    @Column(nullable = false)
    private String description;

    //the code below is for jsonb which is only supported by postgres
    //Once we use postgres then we will uncomment
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb")
//    private List<String> images;
//
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb")
//    private List<String> amenities;

    //The code is supported by H2 console
    @Column(columnDefinition = "TEXT")
    private String amenities;

    @Column(columnDefinition = "TEXT")
    private String images;

}
