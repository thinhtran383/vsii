package space.thinhtran.warehouse.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.thinhtran.warehouse.entity.User;
import space.thinhtran.warehouse.exception.TokenVerificationException;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // write javadoc

    /**
     * Generates a JWT access token for the given user.
     *
     * @param user the user for whom to generate the token
     * @return the generated JWT access token
     */
    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("fullName", user.getFullName())
                .withClaim("role", user.getRole().getRoleName())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secretKey));

    }

    public String getRole(String token) {
        Claim claim = JWT.decode(token).getClaim("role");
        return claim.asString();
    }

    /**
     * Extracts the access token from the given JWT token.
     *
     * @param token the JWT token
     * @return the extracted access token
     */
    public String extractAccessToken(String token) {
        return JWT.decode(token).getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @throws JWTVerificationException if the token is invalid
     */
    public void validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm).build().verify(token);
        } catch (JWTVerificationException e) {
            throw new TokenVerificationException(e.getMessage());
        }
    }
}
