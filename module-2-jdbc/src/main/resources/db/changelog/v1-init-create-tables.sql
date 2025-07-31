--liquibase formatted sql

--changeset kazakova:1 rollback:DROP TABLE IF EXISTS writers
CREATE TABLE IF NOT EXISTS writers (
                                       id         INT AUTO_INCREMENT PRIMARY KEY,
                                       first_name VARCHAR(50) UNIQUE NOT NULL,
                                       last_name  VARCHAR(50) NOT NULL
);

--changeset kazakova:2 rollback:DROP TABLE IF EXISTS labels
CREATE TABLE IF NOT EXISTS labels (
                                      id   INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(50) UNIQUE NOT NULL
);

--changeset kazakova:3 rollback:DROP TABLE IF EXISTS posts
CREATE TABLE IF NOT EXISTS posts (
                                     id        INT AUTO_INCREMENT PRIMARY KEY,
                                     content   TEXT NOT NULL,
                                     created   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     status    VARCHAR(20) NOT NULL,
                                     writer_id INT NOT NULL,
                                     FOREIGN KEY (writer_id) REFERENCES writers (id) ON DELETE CASCADE
);

--changeset kazakova:4 rollback:DROP TABLE IF EXISTS post_labels
CREATE TABLE IF NOT EXISTS post_labels (
                                           post_id  INT NOT NULL,
                                           label_id INT NOT NULL,
                                           PRIMARY KEY (post_id, label_id),
                                           FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
                                           FOREIGN KEY (label_id) REFERENCES labels (id) ON DELETE CASCADE
);

--changeset kazakova:5 rollback:DELETE FROM labels WHERE id IN (1,2,3,4,5)
INSERT INTO labels (name) VALUES
                              ('Важное'),
                              ('Интересное'),
                              ('Жизнь'),
                              ('Спорт'),
                              ('Здоровье');