package space.thinhtran.warehouse.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import space.thinhtran.warehouse.dto.response.ErrorResp;

import java.util.List;

@RestControllerAdvice
@Slf4j
@Hidden
public class ApiExceptionHandler {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, Message: {}, Method: {}";
    private static final String INVALID_REQUEST_INFORMATION_MESSAGE = "Request information is not valid";
    private static final String METHOD_NOT_ALLOWED_MESSAGE_FORMAT =
            "Method %s is not allowed for this endpoint. Supported methods are %s";


    private static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred. Please try again later.";

    @ExceptionHandler(Exception.class) // for all other exceptions debug
    public ResponseEntity<ErrorResp> handleGeneralException(Exception ex, WebRequest request) {
        ex.getStackTrace();
        ex.printStackTrace();

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                DEFAULT_ERROR_MESSAGE, null, ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResp> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                     WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();


        return buildErrorResponse(status, INVALID_REQUEST_INFORMATION_MESSAGE, errors, ex, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResp> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                     WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return buildErrorResponse(status, INVALID_REQUEST_INFORMATION_MESSAGE, null, ex, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResp> handleNotFoundException(NotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResp> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        String message = String.format(METHOD_NOT_ALLOWED_MESSAGE_FORMAT, ex.getMethod(), ex.getSupportedHttpMethods());

        return buildErrorResponse(status, message, null, ex, request);
    }

    @ExceptionHandler(AlreadyExistedException.class)
    public ResponseEntity<ErrorResp> handleAlreadyExistedException(AlreadyExistedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request);
    }

    @ExceptionHandler(TokenVerificationException.class)
    public ResponseEntity<ErrorResp> handleTokenVerificationException(TokenVerificationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ErrorResp> handleNoPermissionException(NoPermissionException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request);
    }


    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<ErrorResp> handleMissingTokenException(MissingTokenException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request);
    }


    private ResponseEntity<ErrorResp> buildErrorResponse(HttpStatus status, String message, List<String> errors,
                                                         Exception ex, WebRequest request) {
        ErrorResp errorVm =
                new ErrorResp(status.toString(), status.getReasonPhrase(), message, errors);

        if (request instanceof ServletWebRequest servletWebRequest) {
            String path = servletWebRequest.getRequest().getServletPath();
            String method = servletWebRequest.getRequest().getMethod();
            log.error(ERROR_LOG_FORMAT, path, message, method);
        } else {
            log.error(message, ex);
        }
        return ResponseEntity.status(status).body(errorVm);
    }

}
