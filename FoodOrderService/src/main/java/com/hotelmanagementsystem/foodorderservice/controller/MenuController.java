package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.MenuItem;
import com.hotelmanagementsystem.foodorderservice.repository.MenuItemRepository;
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

    private final MenuItemRepository menuRepository;  // now entity-based

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> items = menuRepository.findAll();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem,
                                                   HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        MenuItem savedMenu = menuRepository.save(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenu);
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
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody MenuItem updatedMenu,
                                                   HttpServletRequest request) {

        String role = (String) request.getAttribute("X-User-Role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return menuRepository.findById(id)
                .map(menu -> {
                    menu.setName(updatedMenu.getName());
                    menu.setDescription(updatedMenu.getDescription());
                    menu.setPrice(updatedMenu.getPrice());
                    menu.setCategory(updatedMenu.getCategory());
                    menu.setAvailable(updatedMenu.isAvailable());
                    menu.setImages(updatedMenu.getImages());
                    MenuItem savedMenu = menuRepository.save(menu);
                    return ResponseEntity.ok(savedMenu);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
