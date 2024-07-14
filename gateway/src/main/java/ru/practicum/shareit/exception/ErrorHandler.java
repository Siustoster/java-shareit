package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlePreValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleIternalServerError(MethodArgumentTypeMismatchException exception) {
        Class<?> type = exception.getRequiredType();
        String message;

        assert type != null;
        if (type.isEnum() && type.getName().equals("ru.practicum.shareit.booking.BookingState")) {
            message = "Unknown state: UNSUPPORTED_STATUS";
        } else {
            message = String.format("Параметр %s должен быть типом %s", exception.getName(), type.getName());
        }

        return Map.of("error", message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleIternalServerError(Exception exception) {
        return Map.of("error", exception.getMessage());
    }
}