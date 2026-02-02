package com.hotelmanagementsystem.roombookingservice.service;

import com.hotelmanagementsystem.roombookingservice.Repository.RoomRepo;
import com.hotelmanagementsystem.roombookingservice.dto.*;
import com.hotelmanagementsystem.roombookingservice.model.Room;
import com.hotelmanagementsystem.roombookingservice.model.RoomStatus;
import com.hotelmanagementsystem.roombookingservice.model.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepo roomRepo;

    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepo.findByRoomNumber(request.getRoomNumber()).isPresent()) {
            throw new RuntimeException("Room number already exists");
        }

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(RoomType.valueOf(request.getRoomType()));
        room.setPricePerNight(request.getPricePerNight());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
        room.setAmenities(request.getAmenities());
        room.setStatus(RoomStatus.AVAILABLE);

        Room saved = roomRepo.save(room);
        return mapToResponse(saved);
    }

    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomType(RoomType.valueOf(request.getRoomType()));
        room.setPricePerNight(request.getPricePerNight());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
        room.setAmenities(request.getAmenities());

        Room updated = roomRepo.save(room);
        return mapToResponse(updated);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepo.delete(room);
    }

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return mapToResponse(room);
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RoomResponse> getAvailableRooms(RoomAvailabilityRequest request) {
        LocalDate checkIn = LocalDate.parse(request.getCheckInDate());
        LocalDate checkOut = LocalDate.parse(request.getCheckOutDate());

        List<Room> availableRooms = roomRepo.findAvailableRooms(checkIn, checkOut);

        // Filter by room type if specified
        if (request.getRoomType() != null && !request.getRoomType().isEmpty()) {
            RoomType type = RoomType.valueOf(request.getRoomType());
            availableRooms = availableRooms.stream()
                    .filter(room -> room.getRoomType() == type)
                    .collect(Collectors.toList());
        }

        // Filter by capacity if specified
        if (request.getGuests() != null) {
            availableRooms = availableRooms.stream()
                    .filter(room -> room.getCapacity() >= request.getGuests())
                    .collect(Collectors.toList());
        }

        return availableRooms.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoomResponse updateRoomStatus(Long id, RoomStatus status) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setStatus(status);
        Room updated = roomRepo.save(room);
        return mapToResponse(updated);
    }

    public RoomResponse updateRoomPrice(Long id, Double newPrice) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setPricePerNight(newPrice);
        Room updated = roomRepo.save(room);
        return mapToResponse(updated);
    }

    private RoomResponse mapToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setRoomType(room.getRoomType().name());
        response.setPricePerNight(room.getPricePerNight());
        response.setStatus(room.getStatus().name());
        response.setCapacity(room.getCapacity());
        response.setDescription(room.getDescription());
        response.setAmenities(room.getAmenities());
        return response;
    }
}
