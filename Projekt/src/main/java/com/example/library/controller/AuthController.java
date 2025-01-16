package com.example.library.controller;

import java.util.List;
import java.util.Optional;  // Import dla Optional
import com.example.library.model.User;  // Import dla klasy User
import com.example.library.service.UserService;
import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

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
            // Sprawdzenie, czy użytkownik jest administratorem
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getAdmin()) {
                return "redirect:/welcomeAdmin";  // Jeśli admin, przekierowanie do welcomeAdmin
            }
            return "redirect:/welcome";
        } else {
            model.addAttribute("error", "Nieprawidłowy login lub hasło.");
            return "login";
        }
    }

    @GetMapping("/welcomeAdmin")
    public String welcomeAdmin() {
        return "welcomeAdmin";
    }

    // Wyświetla stronę rejestracji
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Obsługa rejestracji
    @PostMapping("/register")
    public String registerUser(String username, String password, Model model, boolean admin) {
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Użytkownik o tej nazwie już istnieje.");
            return "register";
        }

        userService.registerUser(username, password, admin);
        return "redirect:/login";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) {
        List<Book> books = bookService.getAvailableBooks();  // Pobieranie książek
        model.addAttribute("books", books);
        return "welcome";
    }
}
