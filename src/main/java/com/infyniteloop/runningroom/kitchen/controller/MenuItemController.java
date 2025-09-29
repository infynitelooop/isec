package com.infyniteloop.runningroom.kitchen.controller;

import com.infyniteloop.runningroom.kitchen.entity.MenuItem;
import com.infyniteloop.runningroom.kitchen.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.createMenuItem(menuItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable UUID id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable UUID id, @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}