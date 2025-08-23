CREATE TABLE IF NOT EXISTS writers (
                                       id         INT PRIMARY KEY GENERATED ALWAYS AS identity,
                                       first_name VARCHAR(50) UNIQUE NOT NULL,
                                       last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS labels (
                                      id   INT PRIMARY KEY GENERATED ALWAYS AS identity,
                                      name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS posts (
                                     id        INT PRIMARY KEY GENERATED ALWAYS AS identity,
                                     content   TEXT NOT NULL,
                                     created   TIMESTAMP DEFAULT NOW(),
                                     updated   TIMESTAMP DEFAULT NOW(),
                                     status    VARCHAR(20) NOT NULL,
                                     writer_id INT NOT NULL,
                                     FOREIGN KEY (writer_id) REFERENCES writers (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post_labels (
                                           post_id  INT NOT NULL,
                                           label_id INT NOT NULL,
                                           UNIQUE (post_id, label_id),
                                           FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
                                           FOREIGN KEY (label_id) REFERENCES labels (id) ON DELETE CASCADE
);

INSERT INTO labels (name) VALUES
                              ('Важное'),
                              ('Интересное'),
                              ('Жизнь'),
                              ('Спорт'),
                              ('Здоровье');