package com.example.library.service;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Rejestracja nowego użytkownika z hasłem bez szyfrowania
    public void registerUser(String username, String password, boolean admin) {
        User newUser = new User(username, "{noop}" + password, admin);
        userRepository.save(newUser);
}


    // Autentykacja użytkownika
    public boolean authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    

}
