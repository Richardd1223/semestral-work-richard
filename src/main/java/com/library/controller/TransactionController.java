package com.library.controller;

import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.TransactionRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/borrow")
    public String logBorrowTransaction(@RequestParam Long bookId, @RequestParam Long userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (bookOptional.isPresent() && userOptional.isPresent()) {
            Book book = bookOptional.get();
            User user = userOptional.get();

            // Log the borrow transaction
            Transaction borrowTransaction = new Transaction(book, user, LocalDate.now(), "B");
            transactionRepository.save(borrowTransaction);
            return "Borrow transaction logged successfully!";
        }
        return "Book or User not found!";
    }

    @PostMapping("/return")
    public String logReturnTransaction(@RequestParam Long bookId, @RequestParam Long userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (bookOptional.isPresent() && userOptional.isPresent()) {
            Book book = bookOptional.get();
            User user = userOptional.get();

            // Log the return transaction
            Transaction returnTransaction = new Transaction(book, user, LocalDate.now(), "R");
            transactionRepository.save(returnTransaction);
            return "Return transaction logged successfully!";
        }
        return "Book or User not found!";
    }

    // Endpoint to fetch all transactions
    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}


