package com.hotelmanagementsystem.roombookingservice.controller;

import com.hotelmanagementsystem.roombookingservice.Repository.BookingRepo;
import com.hotelmanagementsystem.roombookingservice.dto.BookingRequest;
import com.hotelmanagementsystem.roombookingservice.dto.BookingResponse;
import com.hotelmanagementsystem.roombookingservice.model.Booking;
import com.hotelmanagementsystem.roombookingservice.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(name= "Booking API", description = "This is an Api to create a booking for a Room")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // CUSTOMER/ADMIN - Create booking
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request,
            HttpServletRequest httpRequest) {

        String username = httpRequest.getHeader("X-User-Id");
        return ResponseEntity.ok(bookingService.createBooking(request, username));
    }

    // STAFF/ADMIN - Confirm booking
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    // STAFF/ADMIN - Check-in
    @PatchMapping("/{id}/checkin")
    public ResponseEntity<BookingResponse> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkIn(id));
    }

    // STAFF/ADMIN - Check-out
    @PatchMapping("/{id}/checkout")
    public ResponseEntity<BookingResponse> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkOut(id));
    }

    // STAFF/ADMIN - Get all bookings
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // CUSTOMER - Get my bookings
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(HttpServletRequest request) {
        String username = request.getHeader("X-User-Id");
        return ResponseEntity.ok(bookingService.getBookingsByUsername(username));
    }

    // Get booking by ID with food orders
//    @GetMapping("/{id}")
//    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
//        return ResponseEntity.ok(bookingService.getBookingWithFoodOrders(id));
//    }

    // ADMIN - Cancel booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }
//    BookingRepo bookingRepo;
//
//    public BookingController(BookingRepo bookingRepo) {
//        this.bookingRepo = bookingRepo;
//    }
//
//    @GetMapping
//    public Iterable<Booking> findAll(){
//        return bookingRepo.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Booking findById(@PathVariable Long id){
//        return bookingRepo.findById(id).orElse(null);
//    }
//
//    @PostMapping
//    public Booking create(@RequestBody Booking booking){
//        booking.setId(null);
//        Booking savedBooking = bookingRepo.save(booking);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking).getBody();
//    }
//
//    @PutMapping("/{id}")
//    public Booking update(@PathVariable Long id, @RequestBody Booking booking){
//        Optional<Booking> existing = bookingRepo.findById(id);
//        if(existing.isEmpty()){
//            return null;
//        }
//        booking.setId(id);
//        return bookingRepo.save(booking);
//    }
//
//   @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id){
//       bookingRepo.deleteById(id);
//    }
}
