package com.hotelmanagementsystem.foodorderservice.repository;

import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // JpaRepository already provides findAll(), save(), deleteById(), etc.
}
