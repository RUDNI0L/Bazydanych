package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Pobiera niezarezerwowane ksiazki
    public List<Book> getAvailableBooks() {
        return bookRepository.findByIsReservedFalse();
    }

    // Rezerwuje książkę (JESZCZE NIE DZIALA) ----------------------------------------------------------------TO DO------------
    public boolean reserveBook(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (!book.isReserved()) {
                book.setReserved(true);
                bookRepository.save(book);
                return true;
            }
        }
        return false;
    }
}
