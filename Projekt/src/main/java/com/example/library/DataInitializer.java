package com.example.library;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Dodawanie ksiazek
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Harry Potter i Kamień Filozoficzny", "J.K. Rowling", false));
            bookRepository.save(new Book("Władca Pierścieni", "J.R.R. Tolkien", false));
            bookRepository.save(new Book("Moby Dick", "Herman Melville", false));
            bookRepository.save(new Book("Zbrodnia i kara", "Fiodor Dostojewski", false));
            bookRepository.save(new Book("1984", "George Orwell", false));
        }
    }
}