package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.repository.MenuRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuRepository menuRepository;

    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenuItems() {
        return ResponseEntity.ok(menuRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Menu> createMenuItem(@RequestBody Menu menuItem,
                                                   HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(menuRepository.save(menuItem));
    }

    @DeleteMapping("/{id}")
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
