package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public interface BookingDto {
    int getId();

    User getBooker();

    LocalDateTime getStart();

    LocalDateTime getEnd();
}
