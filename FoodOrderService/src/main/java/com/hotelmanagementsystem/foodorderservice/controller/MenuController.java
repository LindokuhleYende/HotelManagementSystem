package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.repository.MenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Tag(name = "FoodOrder API", description = "This is an API to order food from the menu")
public class MenuController {

    private final MenuRepository menuRepository;

    @GetMapping
    @Operation(summary = "Get all menu items", description = "Retrieves a complete list of all available menu items")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Menu>> getAllMenuItems() {
        return ResponseEntity.ok(menuRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Menu> createMenuItem(@RequestBody Menu menuItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuRepository.save(menuItem));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Menu> updateMenuItem(@PathVariable Long id, @RequestBody Menu updatedMenu) {
        return menuRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setName(updatedMenu.getName());
                    menuItem.setDescription(updatedMenu.getDescription());
                    menuItem.setPrice(updatedMenu.getPrice());
                    menuItem.setCategory(updatedMenu.getCategory());
                    menuItem.setAvailable(updatedMenu.isAvailable());
                    menuItem.setImages(updatedMenu.getImages());
                    return ResponseEntity.ok(menuRepository.save(menuItem));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        if (!menuRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        menuRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
