package com.hotelmanagementsystem.roombookingservice.controller;

import com.hotelmanagementsystem.roombookingservice.dto.RoomAvailabilityRequest;
import com.hotelmanagementsystem.roombookingservice.dto.RoomRequest;
import com.hotelmanagementsystem.roombookingservice.dto.RoomResponse;
import com.hotelmanagementsystem.roombookingservice.model.Room;
import com.hotelmanagementsystem.roombookingservice.Repository.RoomRepo;
import com.hotelmanagementsystem.roombookingservice.model.RoomStatus;
import com.hotelmanagementsystem.roombookingservice.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/rooms")
@Tag(name= "Room API", description = "This is an Api to create a room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // ADMIN only - Create room
    @PostMapping
    @Operation(summary = "Create a room")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    // ADMIN only - Update room
    @PutMapping("/{id}")
    @Operation(summary = "Update room details")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    // ADMIN only - Delete room
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }

    // Public - Get room by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Public - Get all rooms
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Public - Check room availability
    @PostMapping("/availability")
    public ResponseEntity<List<RoomResponse>> checkAvailability(
            @RequestBody RoomAvailabilityRequest request) {
        return ResponseEntity.ok(roomService.getAvailableRooms(request));
    }

    // ADMIN/STAFF - Update room status
    @PatchMapping("/{id}/status")
    public ResponseEntity<RoomResponse> updateRoomStatus(
            @PathVariable Long id,
            @RequestParam RoomStatus status) {
        return ResponseEntity.ok(roomService.updateRoomStatus(id, status));
    }

    // ADMIN - Update room price
    @PatchMapping("/{id}/price")
    public ResponseEntity<RoomResponse> updateRoomPrice(
            @PathVariable Long id,
            @RequestParam Double price) {
        return ResponseEntity.ok(roomService.updateRoomPrice(id, price));
    }
//    private final RoomRepo roomRepo;
//
//    public RoomController(RoomRepo roomRepo) {
//        this.roomRepo = roomRepo;
//    }
//
//    @GetMapping
//    public List<Room> findAll() {
//        return roomRepo.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Room findById(@PathVariable Long id) {
//        return roomRepo.findById(id).orElse(null);
//    }
//
//    @PostMapping
//    public Room create(@RequestBody Room room) {
//        room.setId(null); // ensure create
//        Room savedRoom = roomRepo.save(room);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom).getBody();
//    }
//
//    @PutMapping("/{id}")
//    public Room update(@PathVariable Long id, @RequestBody Room room) {
//        Optional<Room> existing = roomRepo.findById(id);
//        if (existing.isEmpty()) {
//            return null;
//        }
//        room.setId(id);
//        return roomRepo.save(room);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        roomRepo.deleteById(id);
//    }
}
