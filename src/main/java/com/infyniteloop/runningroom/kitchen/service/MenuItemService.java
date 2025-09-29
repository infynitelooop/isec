package com.infyniteloop.runningroom.kitchen.service;

import com.infyniteloop.runningroom.kitchen.entity.MenuItem;
import com.infyniteloop.runningroom.kitchen.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));
    }

    public MenuItem updateMenuItem(UUID id, MenuItem menuItem) {
        MenuItem existing = getMenuItemById(id);

        Optional.ofNullable(menuItem.getName()).ifPresent(existing::setName);
        Optional.ofNullable(menuItem.getDescription()).ifPresent(existing::setDescription);
        Optional.ofNullable(menuItem.getPrice()).ifPresent(existing::setPrice);
        Optional.ofNullable(menuItem.getMealType()).ifPresent(existing::setMealType);
        Optional.ofNullable(menuItem.getMealCategory()).ifPresent(existing::setMealCategory);

        return menuItemRepository.save(existing);
    }

    public void deleteMenuItem(UUID id) {
        menuItemRepository.deleteById(id);
    }
}
