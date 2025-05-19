package space.thinhtran.warehouse.exception;

import space.thinhtran.warehouse.util.MessagesUtil;

import java.lang.module.ResolutionException;

public class AlreadyExistedException extends ResolutionException {
    private final String message;

    public AlreadyExistedException(String errorCode, Object... var2) {
        this.message = MessagesUtil.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
