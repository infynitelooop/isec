package com.infyniteloop.runningroom.kitchen.scheduler;

import com.infyniteloop.runningroom.kitchen.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MenuScheduler {

    private final MenuService menuService;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailyMenu() {
        LocalDate today = LocalDate.now();
        menuService.generateMenuForDate(today);
    }
}