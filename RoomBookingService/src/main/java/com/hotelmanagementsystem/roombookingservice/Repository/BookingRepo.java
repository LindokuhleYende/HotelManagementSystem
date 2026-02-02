package com.hotelmanagementsystem.roombookingservice.Repository;

import com.hotelmanagementsystem.roombookingservice.model.Booking;
import com.hotelmanagementsystem.roombookingservice.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByUsername(String username);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByRoomIdAndStatus(Long roomId, BookingStatus status);
    List<Booking> findByCheckInDateBetween(LocalDate start, LocalDate end);
}
