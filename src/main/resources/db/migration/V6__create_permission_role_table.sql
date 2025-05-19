CREATE TABLE Roles
(
    role_id     INT AUTO_INCREMENT PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE Permissions
(
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    http_method   VARCHAR(10)  NOT NULL,
    api_endpoint  VARCHAR(255) NOT NULL,
    description   VARCHAR(255)
);

CREATE TABLE Role_Permissions
(
    role_id       INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES Roles (role_id),
    FOREIGN KEY (permission_id) REFERENCES Permissions (permission_id)
);
