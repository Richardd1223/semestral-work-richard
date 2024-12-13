package com.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BOOKS")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Enumerated(EnumType.STRING) // Store enum values as strings in the database
    private Status status;

    @ManyToOne
    @JoinColumn(name = "borrower_id", referencedColumnName = "id")
    private User borrower; // Reference to the user who borrowed the book

    public enum Status {
        AVAILABLE,
        BORROWED
    }

    // Default constructor
    public Book() {}

    // Constructor with fields
    public Book(String title, String author, Status status) {
        this.title = title;
        this.author = author;
        this.status = status;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status=" + status +
                ", borrower=" + (borrower != null ? borrower.getName() : "none") +
                '}';
    }
}
