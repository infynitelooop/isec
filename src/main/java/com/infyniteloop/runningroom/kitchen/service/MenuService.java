package com.infyniteloop.runningroom.kitchen.service;


import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.kitchen.entity.Menu;
import com.infyniteloop.runningroom.kitchen.entity.MenuItem;
import com.infyniteloop.runningroom.kitchen.entity.WeeklyMenuItem;
import com.infyniteloop.runningroom.kitchen.entity.WeeklyMenuTemplate;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import com.infyniteloop.runningroom.kitchen.repository.MenuRepository;
import com.infyniteloop.runningroom.kitchen.repository.WeeklyMenuTemplateRepository;
import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Service class for managing menus and weekly menu templates.
 * Provides functionality to generate daily menus based on active weekly templates,
 * retrieve today's menu, and copy weekly menus from templates.
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final WeeklyMenuTemplateRepository templateRepository;
    private final MenuRepository menuRepository;


    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu getMenuById(UUID id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
    }

    @Transactional
    public Menu updateMenu(UUID id, Menu updatedMenu) {
        Menu existingMenu = getMenuById(id);
        existingMenu.getItems().clear();

        for (MenuItem item : updatedMenu.getItems()) {
            item.setMenu(existingMenu); // very important!
            existingMenu.getItems().add(item);
        }

        return menuRepository.save(existingMenu);
    }

    public void deleteMenu(UUID id) {
        menuRepository.deleteById(id);
    }

    /**
     * Retrieves menus within the specified date range, sorted by date and meal type.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return A list of menus within the date range, sorted by date and meal type.
     */
    public List<Menu> getMenusByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Menu> menus = menuRepository.findByMenuDateBetweenOrderByMenuDateAsc(startDate, endDate);
        List<MealType> mealOrder = List.of(MealType.BREAKFAST, MealType.LUNCH, MealType.SNACKS, MealType.DINNER);

        for (Menu menu : menus) {
            menu.getItems().sort(Comparator.comparingInt(item -> mealOrder.indexOf(item.getMealType())));
        }
        return menus;
    }

    public Menu getMenuByDate(LocalDate date) {
        Optional<Menu> menu = menuRepository.findByMenuDate(date);
        List<MealType> mealOrder = List.of(MealType.BREAKFAST, MealType.LUNCH, MealType.SNACKS, MealType.DINNER);


        menu.ifPresent(m -> m.getItems().sort(Comparator.comparingInt(item -> mealOrder.indexOf(item.getMealType()))));

        return menu.orElseThrow(() -> new NotFoundException("Menu not found for date: " + date));
    }


    /**
     * Generates a menu for the specified date based on the active weekly menu template.
     * If a menu already exists for that date, it is returned instead.
     *
     * @param date The date for which to generate the menu.
     * @return The generated or existing menu for the specified date.
     */
    @Transactional
    public Menu generateMenuForDate(LocalDate date) {
        if (menuRepository.existsByMenuDate(date)) {
            return menuRepository.findByMenuDate(date)
                    .orElseThrow(() -> new IllegalStateException("Menu exists but not found!"));
        }

        WeeklyMenuTemplate activeTemplate = templateRepository.findByActiveTrue()
                .orElseThrow(() -> new NotFoundException("No active weekly menu template found"));

        Menu menu = new Menu();
        menu.setMenuDate(date);

        List<MenuItem> items = activeTemplate.getItems().stream()
                .filter(item -> item.getDayOfWeek().equals(date.getDayOfWeek()))
                .map(item -> {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setMealType(item.getMealType());
                    menuItem.setName(item.getName());
                    menuItem.setDescription(item.getDescription());
                    menuItem.setPrice(item.getPrice());
                    menuItem.setMenu(menu);
                    return menuItem;
                })
                .toList();

        menu.setItems(items);

        return menuRepository.save(menu);
    }

    public Menu getTodaysMenu() {
        LocalDate today = LocalDate.now();
        return menuRepository.findByMenuDate(today)
                .orElseGet(() -> generateMenuForDate(today));
    }


    /**
     * Copies a weekly menu from the specified template for the week starting on the given date.
     *
     * @param templateId  The ID of the weekly menu template to copy from.
     * @param startOfWeek The start date of the week (should be a Monday).
     * @return A list of menus created for each day of the week.
     */
    @Transactional
    public List<Menu> copyWeeklyMenuFromTemplate(UUID templateId, LocalDate startOfWeek) {
        WeeklyMenuTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        Map<DayOfWeek, List<WeeklyMenuItem>> itemsByDay = template.getItems()
                .stream()
                .collect(Collectors.groupingBy(WeeklyMenuItem::getDayOfWeek));

        List<Menu> menus = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate menuDate = startOfWeek.with(day);
            Menu menu = new Menu();
            menu.setMenuDate(menuDate);

            for (WeeklyMenuItem templateItem : itemsByDay.getOrDefault(day, List.of())) {
                MenuItem newItem = new MenuItem();
                newItem.setName(templateItem.getName());
                newItem.setDescription(templateItem.getDescription());
                newItem.setPrice(templateItem.getPrice());
                newItem.setMealType(templateItem.getMealType());
                newItem.setMealCategory(null); // template doesn’t have category? set if needed
                newItem.setMenu(menu);
                menu.getItems().add(newItem);
            }
            menus.add(menu);
        }

        return menuRepository.saveAll(menus);
    }

    /**
     * Copies a weekly menu from a historic week to a new target week.
     *
     * @param startOfSourceWeek The start date of the source week (should be a Monday).
     * @param startOfTargetWeek The start date of the target week (should be a Monday).
     * @return A list of menus created for each day of the target week.
     */
    @Transactional
    public List<Menu> copyWeeklyMenuFromHistoricWeek(LocalDate startOfSourceWeek, LocalDate startOfTargetWeek) {

        UUID tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("TenantId not found in request context");
        }

        if (startOfSourceWeek.getDayOfWeek() != DayOfWeek.MONDAY ||
                startOfTargetWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("Start dates must be Mondays");
        }

        // First delete any existing menus in the target week to avoid duplicates
        LocalDate endOfTargetWeek = startOfTargetWeek.plusDays(6);
        List<Menu> existingTargetMenus = menuRepository.findByMenuDateBetweenOrderByMenuDateAsc(startOfTargetWeek, endOfTargetWeek);
        if (!existingTargetMenus.isEmpty()) {
            menuRepository.deleteAll(existingTargetMenus);
        }

        LocalDate endOfSourceWeek = startOfSourceWeek.plusDays(6);

        List<Menu> sourceMenus = menuRepository.findByMenuDateBetweenOrderByMenuDateAsc(startOfSourceWeek, endOfSourceWeek);
        if (sourceMenus.isEmpty()) {
            throw new IllegalStateException("No menus found in source week");
        }

        List<Menu> newMenus = new ArrayList<>();

        for (Menu oldMenu : sourceMenus) {
            int offset = (int) ChronoUnit.DAYS.between(startOfSourceWeek, oldMenu.getMenuDate());
            LocalDate newDate = startOfTargetWeek.plusDays(offset);

            Menu newMenu = addToMenu(oldMenu, newDate);
            newMenu.setTenantId(tenantId);
            newMenus.add(newMenu);
        }

        return menuRepository.saveAll(newMenus);
    }



    @Transactional
    public Menu copyDayMenuFromHistoricDay(LocalDate sourceDate, LocalDate targetDate) {

        UUID tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("TenantId not found in request context");
        }

        // First delete any existing menus in the target week to avoid duplicates
        menuRepository.findByMenuDate(targetDate).ifPresent(menuRepository::delete);

        // Fetch the menu for the source date
        Menu oldMenu = menuRepository.findByMenuDate(sourceDate)
                .orElseThrow(() -> new IllegalStateException("No menu found for source date"));

        // Create a new menu for the target date
        Menu newMenu = addToMenu(oldMenu, targetDate);
        newMenu.setTenantId(tenantId);

        return menuRepository.save(newMenu);
    }

    private Menu addToMenu(Menu oldMenu, LocalDate newDate) {
        Menu newMenu = new Menu();
        newMenu.setMenuDate(newDate);

        for (MenuItem oldItem : oldMenu.getItems()) {
            MenuItem newItem = new MenuItem();
            newItem.setName(oldItem.getName());
            newItem.setDescription(oldItem.getDescription());
            newItem.setPrice(oldItem.getPrice());
            newItem.setMealType(oldItem.getMealType());
            newItem.setMealCategory(oldItem.getMealCategory());
            newItem.setMenu(newMenu);
            newMenu.getItems().add(newItem);
        }
        return newMenu;
    }
}
