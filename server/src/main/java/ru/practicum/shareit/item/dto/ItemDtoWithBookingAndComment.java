package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingDtoForOutItemInformation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoWithBookingAndComment {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @Nullable
    private BookingDtoForOutItemInformation lastBooking;
    @Nullable
    private BookingDtoForOutItemInformation nextBooking;
    @Nullable
    private List<CommentDto> comments;
}
