package com.example.JobHunter.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/")
    public String geString() {
        return "Hello everyone";
    }
}
