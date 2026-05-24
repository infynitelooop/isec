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

        // Clear all fields if name is blank/null, else update normally
        Optional.ofNullable(menuItem.getName())
                .filter(name -> !name.isBlank())
                .ifPresentOrElse(
                        name -> {
                            // If name is present and not blank, update all fields
                            existing.setName(name);
                            Optional.ofNullable(menuItem.getDescription()).ifPresent(existing::setDescription);
                            Optional.ofNullable(menuItem.getPrice()).ifPresent(existing::setPrice);
                            Optional.ofNullable(menuItem.getMealType()).ifPresent(existing::setMealType);
                            Optional.ofNullable(menuItem.getMealCategory()).ifPresent(existing::setMealCategory);
                        },
                        () -> {
                            // If name is blank/null, clear everything except mealCategory
                            existing.setName(null);
                            existing.setDescription(null);
                            existing.setPrice(null);
                            existing.setMealType(null);
                            // Keep mealCategory unchanged
                        }
                );

        return menuItemRepository.save(existing);
    }

    public void deleteMenuItem(UUID id) {
        menuItemRepository.deleteById(id);
    }
}
