package ru.practicum.shareit.exception;

public class ItemIsUnavailableExeption extends RuntimeException {
    public ItemIsUnavailableExeption(String message) {
        super(message);
    }
}
