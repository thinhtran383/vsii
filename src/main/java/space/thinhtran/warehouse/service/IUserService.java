package space.thinhtran.warehouse.service;

import space.thinhtran.warehouse.entity.User;

public interface IUserService {
    User getUserByUsername(String username);
}
