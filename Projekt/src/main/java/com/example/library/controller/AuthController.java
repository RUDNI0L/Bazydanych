package com.example.library.controller;

import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Wyświetla stronę logowania
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Obsługa logowania
    @PostMapping("/login")
    public String loginUser(String username, String password, Model model) {
        boolean isAuthenticated = userService.authenticate(username, password);

        if (isAuthenticated) {
            return "redirect:/welcome";
        } else {
            model.addAttribute("error", "Nieprawidłowy login lub hasło.");
            return "login";
        }
    }

    // Wyświetla stronę rejestracji
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Obsługa rejestracji
    @PostMapping("/register")
    public String registerUser(String username, String password, Model model) {
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Użytkownik o tej nazwie już istnieje.");
            return "register";
        }

        userService.registerUser(username, password);
        return "redirect:/login";
    }

    // Wyświetla stronę powitalną
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
}
