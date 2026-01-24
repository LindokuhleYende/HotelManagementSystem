package com.hotelmanagementsystem.roombookingservice.controller;

import com.hotelmanagementsystem.roombookingservice.Repository.BookingRepo;
import com.hotelmanagementsystem.roombookingservice.model.Booking;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Tag(name= "Booking API", description = "This is an Api to create a booking for a Room")
public class BookingController {
    BookingRepo bookingRepo;

    public BookingController(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @GetMapping
    public Iterable<Booking> findAll(){
        return bookingRepo.findAll();
    }

    @GetMapping("/{id}")
    public Booking findById(@PathVariable Long id){
        return bookingRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking){
        booking.setId(null);
        Booking savedBooking = bookingRepo.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking).getBody();
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking){
        Optional<Booking> existing = bookingRepo.findById(id);
        if(existing.isEmpty()){
            return null;
        }
        booking.setId(id);
        return bookingRepo.save(booking);
    }

   @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
       bookingRepo.deleteById(id);
    }
}
