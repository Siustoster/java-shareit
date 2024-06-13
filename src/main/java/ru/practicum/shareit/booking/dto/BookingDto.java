package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public interface BookingDto {
    int getId();

    User getBooker();

    LocalDateTime getStart();

    LocalDateTime getEnd();
}
