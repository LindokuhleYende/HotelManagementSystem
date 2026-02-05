package com.hotelmanagementsystem.roombookingservice.security;
import com.hotelmanagementsystem.roombookingservice.Repository.BookingRepo;
import com.hotelmanagementsystem.roombookingservice.authConfig.UserPrincipal;
import com.hotelmanagementsystem.roombookingservice.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service("bookingSecurity")
@RequiredArgsConstructor
public class BookingSecurityService {

    private final BookingRepo bookingRepo;

    /**
     * Check if user can modify a booking
     */
    public boolean canModifyBooking(Long bookingId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Admin and Moderator can modify any booking
        if (principal.isAdmin() || principal.isModerator()) {
            return true;
        }

        // Check if booking exists
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            return false;
        }

        // User can only modify their own booking
        if (!booking.getUsername().equals(principal.getUsername())) {
            return false;
        }

        // Check if check-in date is more than 24 hours away
        return booking.getCheckInDate().isAfter(LocalDate.now().plusDays(1));
    }

    /**
     * Check if user can view a booking
     */
    public boolean canViewBooking(Long bookingId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Admin and Moderator can view any booking
        if (principal.isAdmin() || principal.isModerator()) {
            return true;
        }

        // Check if booking exists and belongs to user
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            return false;
        }

        return booking.getUsername().equals(principal.getUsername());
    }

    /**
     * Check if user can cancel a booking
     */
    public boolean canCancelBooking(Long bookingId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Only admin can cancel any booking
        if (principal.isAdmin()) {
            return true;
        }

        // Check if booking exists
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            return false;
        }

        // User can cancel their own booking if check-in is more than 48 hours away
        if (principal.isUser() && booking.getUsername().equals(principal.getUsername())) {
            return booking.getCheckInDate().isAfter(LocalDate.now().plusDays(2));
        }

        return false;
    }

    /**
     * Check if user can create a booking for a specific date range
     */
    public boolean canCreateBooking(LocalDate checkInDate, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Admin can create bookings for any date
        if (principal.isAdmin()) {
            return true;
        }

        // Check if check-in date is in the future
        if (checkInDate.isBefore(LocalDate.now())) {
            return false;
        }

        // Check if booking is not more than 30 days in advance
        return checkInDate.isBefore(LocalDate.now().plusDays(30));
    }

    /**
     * Check if user owns the booking
     */
    public boolean isOwner(Long bookingId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        if (principal.isAdmin()) {
            return true;
        }

        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            return false;
        }

        return booking.getUsername().equals(principal.getUsername());
    }
}

