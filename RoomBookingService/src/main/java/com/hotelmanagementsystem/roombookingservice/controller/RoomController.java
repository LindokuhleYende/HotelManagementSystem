package com.hotelmanagementsystem.roombookingservice.controller;

import com.hotelmanagementsystem.roombookingservice.model.Room;
import com.hotelmanagementsystem.roombookingservice.Repository.RoomRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@Tag(name= "Room API", description = "This is an Api to create a room")
public class RoomController {
    private final RoomRepo roomRepo;

    public RoomController(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    @GetMapping
    public List<Room> findAll() {
        return roomRepo.findAll();
    }

    @GetMapping("/{id}")
    public Room findById(@PathVariable Long id) {
        return roomRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Room create(@RequestBody Room room) {
        room.setId(null); // ensure create
        Room savedRoom = roomRepo.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom).getBody();
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @RequestBody Room room) {
        Optional<Room> existing = roomRepo.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        room.setId(id);
        return roomRepo.save(room);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roomRepo.deleteById(id);
    }
}
