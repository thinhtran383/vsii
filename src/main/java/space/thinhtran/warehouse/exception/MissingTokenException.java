package space.thinhtran.warehouse.exception;

import space.thinhtran.warehouse.util.MessagesUtil;

public class MissingTokenException extends RuntimeException {
    private final String message;

    public MissingTokenException(String errorCode, Object... var2) {
        this.message = MessagesUtil.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
