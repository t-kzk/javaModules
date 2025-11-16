CREATE TABLE auth (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      user_id INT NOT NULL,
                      password_hash VARCHAR(255) NOT NULL,
                      role VARCHAR(50) NOT NULL DEFAULT 'USER',
                      FOREIGN KEY (user_id) REFERENCES users(id)
);
