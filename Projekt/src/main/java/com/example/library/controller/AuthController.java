package com.example.library.controller;

import java.util.List;
import java.util.Optional;

import com.example.library.model.Rental;
import com.example.library.model.User;
import com.example.library.service.RentalService;
import com.example.library.service.UserService;
import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private RentalService rentalService;


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
            // Sprawdzenie czy użytkownik jest admininem
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getAdmin()) {
                return "redirect:/welcomeAdmin";
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


    @PostMapping("/reserve")
    public String reserveBook(Long bookId, Model model) {

        // Pobranie aktualnie zalogowanego użytkownika
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();


        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Nie znaleziono użytkownika.");
            return "welcome";
        }

        User user = userOpt.get();
        Long userId = user.getId();

        boolean success = bookService.rentBook(bookId, userId);

        if (success) {
            model.addAttribute("success", "Książka została pomyślnie zarezerwowana.");
        } else {
            model.addAttribute("error", "Nie udało się zarezerwować książki.");
        }

        // Odświeżenie listy książek
        List<Book> books = bookService.getAvailableBooks();
        model.addAttribute("books", books);
        return "welcome";
    }

    @GetMapping("/yourRentals")
    public String yourRentals(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Nazwa użytkownika
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Wypożyczenia aktualnie zalogowanego uzytkownika
            List<Rental> rentals = rentalService.getRentalsByUserId(user.getId());

            model.addAttribute("rentals", rentals);
        } else {
            model.addAttribute("error", "Nie znaleziono użytkownika.");
        }

        return "yourRentals";
    }

    @PostMapping("/returnBook")
    public String returnBook(Long rentalId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            boolean success = bookService.returnBook(rentalId);
            if (success) {
                model.addAttribute("success", "Książka została pomyślnie oddana.");
            } else {
                model.addAttribute("error", "Nie udało się oddać książki.");
            }

            List<Rental> rentals = rentalService.getRentalsByUserId(user.getId());
            model.addAttribute("rentals", rentals);
        } else {
            model.addAttribute("error", "Nie znaleziono użytkownika.");
        }

        return "yourRentals";
    }


}
