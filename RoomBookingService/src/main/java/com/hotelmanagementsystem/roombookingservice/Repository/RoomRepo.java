package com.hotelmanagementsystem.roombookingservice.Repository;

import com.hotelmanagementsystem.roombookingservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    @Override
    List<Room> findAll();

}
