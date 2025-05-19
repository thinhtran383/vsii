package space.thinhtran.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import space.thinhtran.warehouse.util.MessagesUtil;

public class TokenVerificationException extends RuntimeException {
    private final String message;

    public TokenVerificationException(String errorCode, Object... var2) {
        this.message = MessagesUtil.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
