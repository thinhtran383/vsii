package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.entity.User;
import space.thinhtran.warehouse.exception.NotFoundException;
import space.thinhtran.warehouse.repository.UserRepository;
import space.thinhtran.warehouse.service.IUserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve
     * @return The user information
     * @throws NotFoundException If the user with the given username is not found
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.USER_NOT_FOUND, username));
    }
}
