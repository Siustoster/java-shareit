package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
    public interface BookingDto {
    int getId();
    User getBooker();
    LocalDateTime getStart();
    LocalDateTime getEnd();
}
