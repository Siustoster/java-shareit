package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingInputDto {
    private int itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
