package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.repository.MenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Tag(name = "FoodOrder API", description = "This is an API to order food from the menu")
public class MenuController {

    private final MenuRepository menuRepository;

    @GetMapping
    @Operation(summary = "Get's all menu items", description = "Retrieves a complete list of all available menu items from the system, including their names, categories, and prices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of menu items"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Menu>> getAllMenuItems() {
        return ResponseEntity.ok(menuRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a menu item", description = "Adds a new menu item to the system. Accepts details such as name, description, category, and price in the request body, and returns the created item with its assigned ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu item successfully created"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have permission"),
            @ApiResponse(responseCode = "400", description = "Bad request – invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Menu> createMenuItem(@RequestBody Menu menuItem,
                                                   HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(menuRepository.save(menuItem));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu item", description = "Deletes a menu item from the system by its UNIQUE ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu item successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden – only ADMIN can delete"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id,
                                               HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!menuRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        menuRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a menu item", description = "Updates a menu item in the system by its UNIQUE ID. Accepts details such as name, description, category, and price in the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item successfully updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have permission"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "400", description = "Bad request – invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Menu> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody Menu updatedMenu,
                                                   HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
}
