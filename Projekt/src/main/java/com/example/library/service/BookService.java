package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.example.library.model.User;
import com.example.library.model.Rental;
import com.example.library.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserService userService;


    public List<Book> getAvailableBooks() {
        return bookRepository.findByIsReservedFalse();
    }

    // Rezerwacja książki i zapisanie wypożyczenia
    public boolean rentBook(Long bookId, Long userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<User> userOptional = userService.findById(userId);

        if (bookOptional.isPresent() && userOptional.isPresent()) {
            Book book = bookOptional.get();
            User user = userOptional.get();

            if (!book.isReserved()) {

                book.setReserved(true);
                bookRepository.save(book);

                Rental rental = new Rental(user, book);
                rentalRepository.save(rental);

                return true;
            }
        }

        return false;
    }

    public boolean returnBook(Long rentalId) {
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalId);

        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            Book book = rental.getBook();

            book.setReserved(false);
            bookRepository.save(book);

            rentalRepository.delete(rental);
            return true;
        }
        return false;  // Jeśli wypożyczenie nie istnieje
    }
}
