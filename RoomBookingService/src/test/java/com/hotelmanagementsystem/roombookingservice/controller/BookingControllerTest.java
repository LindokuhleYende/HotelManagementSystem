package com.hotelmanagementsystem.roombookingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanagementsystem.roombookingservice.dto.BookingRequest;
import com.hotelmanagementsystem.roombookingservice.dto.BookingResponse;
import com.hotelmanagementsystem.roombookingservice.service.BookingService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= CREATE BOOKING =================

    @Test
    @WithMockUser(roles = "USER")
    void createBooking_shouldReturnBooking() throws Exception {

        BookingRequest request = new BookingRequest();
        request.setCheckInDate("2026-02-10");
        request.setCheckOutDate("2026-02-12");
        request.setRoomId(1L);

        BookingResponse response = new BookingResponse();
        response.setId(1L);
        response.setStatus("CONFIRMED");

        Mockito.when(bookingService.createBooking(
                Mockito.any(),
                Mockito.anyString()
        )).thenReturn(response);

        mockMvc.perform(post("/api/bookings")
                        .header("X-User-Id", "lindo123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    // ================= CONFIRM BOOKING =================

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmBooking_shouldReturnUpdatedBooking() throws Exception {

        BookingResponse response = new BookingResponse();
        response.setId(1L);
        response.setStatus("CONFIRMED");

        Mockito.when(bookingService.confirmBooking(1L))
                .thenReturn(response);

        mockMvc.perform(patch("/api/bookings/1/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    // ================= CHECK-IN =================

    @Test
    @WithMockUser(roles = "MODERATOR")
    void checkIn_shouldReturnCheckedInBooking() throws Exception {

        BookingResponse response = new BookingResponse();
        response.setStatus("CHECKED_IN");

        Mockito.when(bookingService.checkIn(1L))
                .thenReturn(response);

        mockMvc.perform(patch("/api/bookings/1/checkin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CHECKED_IN"));
    }

    // ================= CHECK-OUT =================

    @Test
    @WithMockUser(roles = "MODERATOR")
    void checkOut_shouldReturnCheckedOutBooking() throws Exception {

        BookingResponse response = new BookingResponse();
        response.setStatus("CHECKED_OUT");

        Mockito.when(bookingService.checkOut(1L))
                .thenReturn(response);

        mockMvc.perform(patch("/api/bookings/1/checkout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CHECKED_OUT"));
    }

    // ================= GET ALL BOOKINGS =================

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBookings_shouldReturnList() throws Exception {

        BookingResponse b1 = new BookingResponse();
        b1.setId(1L);

        BookingResponse b2 = new BookingResponse();
        b2.setId(2L);

        Mockito.when(bookingService.getAllBookings())
                .thenReturn(List.of(b1, b2));

        mockMvc.perform(get("/api/bookings/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ================= MY BOOKINGS =================

    @Test
    @WithMockUser
    void getMyBookings_shouldReturnUserBookings() throws Exception {

        BookingResponse response = new BookingResponse();
        response.setId(1L);

        Mockito.when(bookingService.getBookingsByUsername("lindo123"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/bookings/my-bookings")
                        .header("X-User-Id", "lindo123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ================= CANCEL BOOKING =================

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelBooking_shouldReturnOk() throws Exception {

        Mockito.doNothing()
                .when(bookingService)
                .cancelBooking(1L);

        mockMvc.perform(put("/api/bookings/1/cancel"))
                .andExpect(status().isOk());
    }

    // ================= FORCE CANCEL =================

    @Test
    @WithMockUser(roles = "ADMIN")
    void forceCancelBooking_shouldReturnOk() throws Exception {

        Mockito.doNothing()
                .when(bookingService)
                .cancelBooking(1L);

        mockMvc.perform(delete("/api/bookings/1/force-cancel"))
                .andExpect(status().isOk());
    }
}
