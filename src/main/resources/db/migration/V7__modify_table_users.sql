ALTER TABLE Users
DROP COLUMN role_name;

ALTER TABLE Users
    ADD COLUMN role_id INT;

ALTER TABLE Users
    ADD FOREIGN KEY (role_id) REFERENCES Roles(role_id);
