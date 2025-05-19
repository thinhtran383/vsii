ALTER TABLE Orders
    ADD FOREIGN KEY (created_by) REFERENCES Users(user_id);
ALTER TABLE OrderDetails
    ADD FOREIGN KEY (created_by) REFERENCES Users(user_id);
