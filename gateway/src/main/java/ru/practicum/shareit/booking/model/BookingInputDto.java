package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingInputDto {
    private Integer itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
