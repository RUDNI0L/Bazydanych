package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

   
    @GetMapping("/login")
    public String login() {
        return "login";
    }

   
    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "welcome"; 
    }


    // Logika rejestracji
    @PostMapping("/register")
    public String registerUser(String username, String password, Model model) {
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Użytkownik o tej nazwie już istnieje.");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userRepository.save(newUser);

        return "redirect:/login";
    }
}
