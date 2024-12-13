package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate transactionDate;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // "B" for Borrow, "R" for Return

    public Transaction() {}

    public Transaction(Book book, User user, LocalDate transactionDate, String transactionType) {
        this.book = book;
        this.user = user;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", book=" + book.getTitle() +
                ", user=" + user.getName() +
                ", transactionDate=" + transactionDate +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}

