-- Users Table
CREATE TABLE user (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(20) NOT NULL
);


-- Books Table
CREATE TABLE books (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       status VARCHAR(20) NOT NULL
);

-- Transactions Table
CREATE TABLE transactions (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              book_id INT NOT NULL,
                              user_id INT NOT NULL,
                              transaction_date VARCHAR(255),
                              FOREIGN KEY (book_id) REFERENCES books(id),
                              FOREIGN KEY (user_id) REFERENCES users(id)
);

