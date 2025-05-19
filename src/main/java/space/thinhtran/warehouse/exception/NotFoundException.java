package space.thinhtran.warehouse.exception;

import space.thinhtran.warehouse.util.MessagesUtil;

public class NotFoundException extends RuntimeException {
    private final String message;

    public NotFoundException(String errorCode, Object... var2) {
        this.message = MessagesUtil.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
