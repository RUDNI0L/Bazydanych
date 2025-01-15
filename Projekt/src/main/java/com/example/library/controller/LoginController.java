package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


@GetMapping("/welcome")
    public String welcome() {
        return "welcome";  // Strona powitalna (welcome.html)
    }
}