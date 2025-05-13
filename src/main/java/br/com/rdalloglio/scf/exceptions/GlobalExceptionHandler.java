package br.com.rdalloglio.scf.exceptions;

import static br.com.rdalloglio.scf.constants.CategoryMessageKeys.INVALID_REQUEST;
import static br.com.rdalloglio.scf.constants.CategoryMessageKeys.TYPE_INVALID;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.rdalloglio.scf.enums.CategoryType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidEnum(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        if (ex.getCause() instanceof InvalidFormatException formatException) {
            String fieldName = formatException.getPath().get(0).getFieldName();
            String value = formatException.getValue().toString();
            String acceptedValues = Arrays.toString(CategoryType.values());

            String message = messageSource.getMessage(
                    TYPE_INVALID,
                    new Object[] { value, fieldName, acceptedValues },
                    Locale.getDefault());

            error.put(fieldName, message);
        } else {
            String message = messageSource.getMessage(
                    INVALID_REQUEST,
                    null,
                    Locale.getDefault());
            error.put("error", message);
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();

        if (ex.getRequiredType() == CategoryType.class) {
            String fieldName = ex.getName();
            String value = java.util.Objects.toString(ex.getValue(), "null");
            String acceptedValues = Arrays.toString(CategoryType.values());

            String message = messageSource.getMessage(
                    TYPE_INVALID,
                    new Object[] { value, fieldName, acceptedValues },
                    Locale.getDefault());

            error.put(fieldName, message);
        } else {
            String message = messageSource.getMessage(
                    INVALID_REQUEST,
                    null,
                    Locale.getDefault());
            error.put("error", message);
        }

        return error;
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleCategoryNotFound(CategoryNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Conflict",
                        "message", ex.getMessage(),
                        "path", request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", ex.getMessage(),
                        "path", request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // 401
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 401,
                        "error", "Unauthorized",
                        "message", ex.getMessage(),
                        "path", request.getDescription(false).replace("uri=", "")));
    }

}