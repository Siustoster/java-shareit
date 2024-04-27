package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleExceptionEmailExists(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleExceptionNotFound(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleExceptionOther(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(WrongOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleExceptionWrongOwner(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }
}
