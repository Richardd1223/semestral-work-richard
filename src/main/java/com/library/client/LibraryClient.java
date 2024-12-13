package com.library.client;

import com.library.model.Book;
import com.library.model.User;
import com.library.model.User.Role;
import org.springframework.web.client.RestTemplate;
import com.library.model.Transaction;
import java.util.Scanner;

public class LibraryClient {

    private static final String USER_BASE_URL = "http://localhost:8000/users";
    private static final String BOOK_BASE_URL = "http://localhost:8000/books";

    public static void main(String[] args) {
        LibraryClient client = new LibraryClient();
        client.showMainMenu();
    }

    public void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Library Management System - Main Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Removed role input, will always be STUDENT
        User user = new User(name, password, User.Role.STUDENT);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.postForObject(USER_BASE_URL + "/register", user, String.class);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private void loginUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        RestTemplate restTemplate = new RestTemplate();
        try {
            User user = restTemplate.getForObject(USER_BASE_URL + "/login?name=" + name + "&password=" + password, User.class);
            if (user != null) {
                System.out.println("Login successful! Welcome, " + user.getName());
                if (user.getRole() == Role.ADMIN) {
                    showAdminMenu();
                } else {
                    showStudentMenu(user);
                }
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    private void showAdminMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add a Book");
            System.out.println("2. View All Books");
            System.out.println("3. Delete a Book");
            System.out.println("4. Edit a Book");
            System.out.println("5. Register a New User");
            System.out.println("6. View Users");
            System.out.println("7. Delete a User");
            System.out.println("8. Edit a User");
            System.out.println("9. View Transactions");
            System.out.println("10. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> deleteBook();
                case 4 -> editBook();
                case 5 -> registerUser();
                case 6 -> viewUsers();
                case 7 -> deleteUser();
                case 8 -> editUser();
                case 9 -> viewTransactions();
                case 10 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewTransactions() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Transaction[] transactions = restTemplate.getForObject("http://localhost:8000/transactions/all", Transaction[].class);
            System.out.println("Transaction Records:");
            if (transactions != null && transactions.length > 0) {
                for (Transaction transaction : transactions) {
                    System.out.println(transaction);
                }
            } else {
                System.out.println("No transactions found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
    }

    private void deleteUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the user ID to delete: ");
        Long userId = scanner.nextLong();

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete("http://localhost:8000/users/delete/" + userId);
            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private void editUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the user ID to edit: ");
        Long userId = scanner.nextLong();
        scanner.nextLine(); // consume the newline character

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        System.out.print("Enter new role (ADMIN/STUDENT): ");
        String roleInput = scanner.nextLine();

        User.Role role;
        try {
            role = User.Role.valueOf(roleInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role. Please enter ADMIN or STUDENT.");
            return;
        }

        User updatedUser = new User(name, password, role);

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Use PUT to send the updated user details
            restTemplate.put(USER_BASE_URL + "/edit/" + userId, updatedUser);
            System.out.println("User updated successfully!");
        } catch (Exception e) {
            System.out.println("Error editing user: " + e.getMessage());
        }
    }

    private void editBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the book ID to edit: ");
        Long bookId = scanner.nextLong();
        scanner.nextLine(); // consume the newline character

        System.out.print("Enter new title: ");
        String title = scanner.nextLine();

        System.out.print("Enter new author: ");
        String author = scanner.nextLine();

        System.out.print("Enter new status (AVAILABLE/BORROWED): ");
        String statusInput = scanner.nextLine();

        Book.Status status;
        try {
            status = Book.Status.valueOf(statusInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter AVAILABLE or BORROWED.");
            return;
        }

        Book updatedBook = new Book();
        updatedBook.setTitle(title);
        updatedBook.setAuthor(author);
        updatedBook.setStatus(status);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.put(BOOK_BASE_URL + "/edit/" + bookId, updatedBook);
            System.out.println("Book updated successfully!");
        } catch (Exception e) {
            System.out.println("Error editing book: " + e.getMessage());
        }
    }

    private void showStudentMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Student Menu:");
            System.out.println("1. View All Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book");
            System.out.println("4. View Borrowed Books");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1 -> viewAvailableBooks();
                case 2 -> borrowBook(user);
                case 3 -> returnBook(user);
                case 4 -> viewBorrowedBooks(user);
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        // Create a book with default status AVAILABLE
        Book book = new Book(title, author, Book.Status.AVAILABLE);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.postForObject(BOOK_BASE_URL + "/add", book, String.class);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    private void viewBooks() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Book[] books = restTemplate.getForObject(BOOK_BASE_URL + "/all", Book[].class);
            System.out.println("Books in the library:");
            if (books != null) {
                for (Book book : books) {
                    System.out.println(book);
                }
            } else {
                System.out.println("No books available.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving books: " + e.getMessage());
        }
    }

    private void viewAvailableBooks() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Book[] books = restTemplate.getForObject(BOOK_BASE_URL + "/available", Book[].class);
            System.out.println("Available books in the library:");
            if (books != null) {
                for (Book book : books) {
                    System.out.println(book);
                }
            } else {
                System.out.println("No available books.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving available books: " + e.getMessage());
        }
    }

    private void borrowBook(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the book ID to borrow: ");
        Long bookId = scanner.nextLong();

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.postForObject(BOOK_BASE_URL + "/borrow/" + bookId + "?userId=" + user.getId(), null, String.class);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error borrowing book: " + e.getMessage());
        }
    }

    private void returnBook(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the book ID to return: ");
        Long bookId = scanner.nextLong();

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Using PUT method to match the server's endpoint
            restTemplate.put(BOOK_BASE_URL + "/return/" + bookId + "?userId=" + user.getId(), null);
            System.out.println("Book returned successfully!");
        } catch (Exception e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }

    private void viewBorrowedBooks(User user) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Book[] books = restTemplate.getForObject(BOOK_BASE_URL + "/borrowed/" + user.getId(), Book[].class);
            System.out.println("Books you have borrowed:");
            if (books != null) {
                for (Book book : books) {
                    System.out.println(book);
                }
            } else {
                System.out.println("You have not borrowed any books.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving borrowed books: " + e.getMessage());
        }
    }

    private void deleteBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the book ID to delete: ");
        Long bookId = scanner.nextLong();

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(BOOK_BASE_URL + "/delete/" + bookId);
            System.out.println("Book deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    private void viewUsers() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            User[] users = restTemplate.getForObject(USER_BASE_URL + "/all", User[].class);
            System.out.println("Registered users:");
            if (users != null) {
                for (User user : users) {
                    System.out.println(user);
                }
            } else {
                System.out.println("No users found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }
}

