CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(255) NOT NULL,
                       status VARCHAR(50) NOT NULL
);

CREATE TABLE files (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       location VARCHAR(500) NOT NULL,
                       status VARCHAR(50) NOT NULL
);

CREATE TABLE events (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT,
                        file_id INT,
                        status VARCHAR(50) NOT NULL,
                        timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (file_id) REFERENCES files(id)
);