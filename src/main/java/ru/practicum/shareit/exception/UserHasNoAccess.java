package ru.practicum.shareit.exception;

public class UserHasNoAccess extends RuntimeException {
    public UserHasNoAccess(String message) {
        super(message);
    }
}