package com.hotelmanagementsystem.roombookingservice.Repository;

import com.hotelmanagementsystem.roombookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    @Override
    List<Booking> findAll();

    @Override
    List<Booking> findAllById(Iterable<Long> longs);
}
