package com.infyniteloop.isec.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "Welcome to ISEC";
    }
}
