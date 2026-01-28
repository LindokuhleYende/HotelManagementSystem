package com.hotelmanagementsystem.foodservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.hotelmanagementsystem.foodservice.entity.MenuItem;


public interface MenuRepo extends JpaRepository<MenuItem, Long> {
    @Override
    List<MenuItem> findAll();
}

