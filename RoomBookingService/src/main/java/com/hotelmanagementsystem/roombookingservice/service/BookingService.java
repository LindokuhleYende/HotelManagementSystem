package com.hotelmanagementsystem.roombookingservice.service;

import com.hotelmanagementsystem.roombookingservice.Repository.BookingRepo;
import com.hotelmanagementsystem.roombookingservice.Repository.RoomRepo;
import com.hotelmanagementsystem.roombookingservice.dto.BookingRequest;
import com.hotelmanagementsystem.roombookingservice.dto.BookingResponse;
import com.hotelmanagementsystem.roombookingservice.dto.RoomResponse;
import com.hotelmanagementsystem.roombookingservice.model.Booking;
import com.hotelmanagementsystem.roombookingservice.model.BookingStatus;
import com.hotelmanagementsystem.roombookingservice.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepository;
    private final RoomRepo roomRepo;

//    @Autowired
//    private FoodOrderClient foodOrderClient;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String username) {
        Room room = roomRepo.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Validate room capacity
        if (request.getNumberOfGuests() > room.getCapacity()) {
            throw new RuntimeException("Number of guests exceeds room capacity");
        }

        // Check room availability
        LocalDate checkIn = LocalDate.parse(request.getCheckInDate());
        LocalDate checkOut = LocalDate.parse(request.getCheckOutDate());

        if (checkIn.isBefore(LocalDate.now())) {
            throw new RuntimeException("Check-in date cannot be in the past");
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }

        List<Booking> conflictingBookings = bookingRepository
                .findByRoomIdAndStatus(room.getId(), BookingStatus.CONFIRMED);

        for (Booking existing : conflictingBookings) {
            if (!(checkOut.isBefore(existing.getCheckInDate()) ||
                    checkIn.isAfter(existing.getCheckOutDate()))) {
                throw new RuntimeException("Room is not available for selected dates");
            }
        }

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setGuestPhone(request.getGuestPhone());
        booking.setUsername(username);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setBookingTime(LocalDateTime.now());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setSpecialRequests(request.getSpecialRequests());

        // Calculate total price
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        booking.setTotalPrice(nights * room.getPricePerNight());

        booking.setStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public BookingResponse checkIn(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be checked in");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public BookingResponse checkOut(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new RuntimeException("Only checked-in bookings can be checked out");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByUsername(String username) {
        return bookingRepository.findByUsername(username).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

//    public BookingResponse getBookingWithFoodOrders(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        // Use Feign client to get food orders
//        try {
//            List<FoodOrderDTO> foodOrders = foodOrderClient.getFoodOrdersByBooking(bookingId);
//            // You can add food orders to response if needed
//        } catch (Exception e) {
//            // Handle feign client errors
//        }
//
//        return mapToResponse(booking);
//    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CHECKED_IN ||
                booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new RuntimeException("Cannot cancel booking in current status");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());

        // Map room details
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(booking.getRoom().getId());
        roomResponse.setRoomNumber(booking.getRoom().getRoomNumber());
        roomResponse.setRoomType(booking.getRoom().getRoomType().name());
        roomResponse.setPricePerNight(booking.getRoom().getPricePerNight());
        response.setRoom(roomResponse);

        response.setGuestName(booking.getGuestName());
        response.setGuestEmail(booking.getGuestEmail());
        response.setGuestPhone(booking.getGuestPhone());
        response.setUsername(booking.getUsername());
        response.setCheckInDate(booking.getCheckInDate().toString());
        response.setCheckOutDate(booking.getCheckOutDate().toString());
        response.setBookingTime(booking.getBookingTime().toString());
        response.setNumberOfGuests(booking.getNumberOfGuests());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus().name());
        response.setSpecialRequests(booking.getSpecialRequests());

        return response;
    }
}
