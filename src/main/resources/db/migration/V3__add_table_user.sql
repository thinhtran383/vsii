CREATE TABLE Users (
                       user_id    INT AUTO_INCREMENT PRIMARY KEY,
                       username   VARCHAR(100) NOT NULL UNIQUE,
                       password   VARCHAR(255) NOT NULL,
                       full_name  VARCHAR(255),
                       role_name    varchar(20),
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT NULL
);
