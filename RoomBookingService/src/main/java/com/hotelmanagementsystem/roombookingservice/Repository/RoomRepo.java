package com.hotelmanagementsystem.roombookingservice.Repository;

import com.hotelmanagementsystem.roombookingservice.model.Room;
import com.hotelmanagementsystem.roombookingservice.model.RoomStatus;
import com.hotelmanagementsystem.roombookingservice.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByStatus(RoomStatus status);
    List<Room> findByRoomType(RoomType roomType);

    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' " +
            "AND r.id NOT IN (SELECT b.room.id FROM Booking b " +
            "WHERE b.status IN ('CONFIRMED', 'CHECKED_IN') " +
            "AND ((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);

}
