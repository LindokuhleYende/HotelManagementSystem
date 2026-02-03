package com.hotelmanagementsystem.foodorderservice.controller;

import com.hotelmanagementsystem.foodorderservice.entity.Menu;
import com.hotelmanagementsystem.foodorderservice.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuItemRepository menuRepository;

    public MenuController(MenuItemRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @PostMapping
    public Menu createMenuItem(@RequestBody Menu menu) {
        // Now category and images will be saved correctly
        return menuRepository.save(menu);
    }
@DeleteMapping
public Menu deleteMenuItem(@RequestBody Menu menu){
        menuRepository.delete(menu);
        return menu;
}
    @GetMapping
    public List<Menu> getAllMenuItems() {
        return menuRepository.findAll();
    }
}
