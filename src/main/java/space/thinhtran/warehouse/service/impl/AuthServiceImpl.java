package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.dto.request.auth.LoginReq;
import space.thinhtran.warehouse.dto.request.auth.RegisterReq;
import space.thinhtran.warehouse.dto.response.auth.LoginResp;
import space.thinhtran.warehouse.dto.response.auth.RegisterResp;
import space.thinhtran.warehouse.entity.Role;
import space.thinhtran.warehouse.entity.User;
import space.thinhtran.warehouse.exception.AlreadyExistedException;
import space.thinhtran.warehouse.repository.UserRepository;
import space.thinhtran.warehouse.service.IAuthService;
import space.thinhtran.warehouse.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    /**
     * Creates a new user account with STAFF role.
     *
     * @param req The registration request containing user information
     * @return The created user's information
     * @throws AlreadyExistedException If a user with the same username already exists
     */
    @Transactional
    public RegisterResp createUser(RegisterReq req) {
        boolean isExisted = userRepository.existsByUsername(req.getUsername());

        if (isExisted) {
            throw new AlreadyExistedException(Constant.ErrorCode.USER_ALREADY_EXISTS, req.getUsername());
        }

        User newUser = modelMapper.map(req, User.class);

        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.getRole().setId(1);

        return RegisterResp.of(userRepository.save(newUser));
    }

    /**
     * Authenticates a user and generates an access token.
     *
     * @param req The login request containing username and password
     * @return The login response with user information and access token
     * @throws BadCredentialsException If the username or password is incorrect
     */
    @Transactional(readOnly = true)
    public LoginResp login(LoginReq req) {
        User user = userRepository.findByUsername(req.getUsername()).orElse(null);

        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(Constant.ErrorCode.USER_BAD_CREDENTIALS);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                req.getUsername(),
                req.getPassword(),
                user.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        return LoginResp.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .accessToken(jwtUtil.generateAccessToken(user))
                .role(user.getRole().getRoleName())
                .build();

    }


}
