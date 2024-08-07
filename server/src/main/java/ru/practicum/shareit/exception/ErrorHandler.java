package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleExceptionNotFound(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "Error message", e.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleExceptionOther(final RuntimeException e) {
        return Map.of(
                "error", e.getMessage(),
                "Error message", e.getMessage()
        );
    }

    @ExceptionHandler(WrongOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleExceptionWrongOwner(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "Error message", e.getMessage()
        );
    }

    @ExceptionHandler({ItemIsUnavailableExeption.class, BadDateException.class, BadParameterException.class,
            UserHasNoAccess.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleExceptionBadRequest(final RuntimeException e) {
        return Map.of(
                "error", e.getMessage(),
                "Error message", e.getMessage()
        );
    }
}
