package com.library.controller;

import jakarta.persistence.EntityManager;
import com.library.model.Book;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.PersistenceContext;
import com.library.model.Transaction;
import com.library.repository.TransactionRepository;
import java.time.LocalDate;

import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Endpoint to get all books
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    // Endpoint to add a new book
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        book.setStatus(Book.Status.AVAILABLE); // Ensure new books are available by default
        bookRepository.save(book);
        return ResponseEntity.ok("Book added successfully!");
    }

    // Endpoint to delete a book by ID
    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            bookRepository.delete(bookOptional.get());
            return ResponseEntity.ok("Book deleted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Book not found!");
        }
    }

    // Endpoint to get all available books
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookRepository.findByStatus(Book.Status.AVAILABLE);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, @RequestParam Long userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (book.getStatus() == Book.Status.AVAILABLE) {
                book.setStatus(Book.Status.BORROWED);
                User user = userRepository.findById(userId).orElse(null);
                book.setBorrower(user);
                bookRepository.save(book);

                // Log the borrow transaction
                if (user != null) {
                    Transaction borrowTransaction = new Transaction(book, user, LocalDate.now(), "B");
                    transactionRepository.save(borrowTransaction);
                }

                return "Book borrowed successfully!";
            } else {
                return "Book is not available!";
            }
        } else {
            return "Book not found!";
        }
    }

    // Endpoint to return a borrowed book
    @PutMapping("/return/{bookId}")
    public String returnBook(@PathVariable Long bookId, @RequestParam Long userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (book.getBorrower() != null && book.getBorrower().getId().equals(userId)) {
                book.setStatus(Book.Status.AVAILABLE);
                book.setBorrower(null);
                bookRepository.save(book);

                // Log the return transaction
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    Transaction returnTransaction = new Transaction(book, user, LocalDate.now(), "R");
                    transactionRepository.save(returnTransaction);
                }

                return "Book returned successfully!";
            } else {
                return "Book was not borrowed by this user!";
            }
        } else {
            return "Book not found!";
        }
    }


    @GetMapping("/borrowed/{userId}")
    public List<Book> getBorrowedBooks(@PathVariable Long userId) {
        List<Book> books = bookRepository.findAll(); // Fetch all books
        List<Book> borrowedBooks = new ArrayList<>(); // Prepare a list to store borrowed books

        // Filter books manually
        for (Book book : books) {
            if (book.getBorrower() != null && book.getBorrower().getId().equals(userId)) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks; // Return the filtered list of borrowed books
    }

}
