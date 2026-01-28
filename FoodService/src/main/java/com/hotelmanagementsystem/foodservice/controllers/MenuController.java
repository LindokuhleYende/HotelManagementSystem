package com.hotelmanagementsystem.foodservice.controllers;

//Menu Controller will be used to create a menu item by admin and moderators
//Guests will be able to view the menu items

import com.hotelmanagementsystem.foodservice.repository.MenuRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hotelmanagementsystem.foodservice.entity.MenuItem;

import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@Tag(name= "Menu API", description = "This is an Api to create a menu item, view and update menu items")
public class MenuController {
    private final MenuRepo menuRepo;

    public MenuController(MenuRepo menuRepo) {
        this.menuRepo = menuRepo;
    }

    @PostMapping
    public MenuItem createMenu(@RequestBody MenuItem menuItem){
        menuItem.setId(null);
        MenuItem savedMenuItem = menuRepo.save(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem).getBody();
    }

    @GetMapping
    public Iterable<MenuItem> getAllMenuItems(){
        return menuRepo.findAll();
    }

    @GetMapping("/{id}")
    public MenuItem getMenuItemById(@PathVariable Long id){
        return menuRepo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public MenuItem updateMenu(@PathVariable Long id, @RequestBody MenuItem menuItem){
        Optional<MenuItem> existing = menuRepo.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        menuItem.setId(id);
        MenuItem updatedMenuItem = menuRepo.save(menuItem);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMenuItem).getBody();
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id){
        if(menuRepo.existsById(id)){
            menuRepo.deleteById(id);
        }
    }
}
