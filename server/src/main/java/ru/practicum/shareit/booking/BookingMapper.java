package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForOutItemInformation;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Component
public class BookingMapper {
    public Booking mapToBooking(BookingInputDto bookingInput, User user, Item item, BookingStatus status) {
        return new Booking(bookingInput.getStart(), bookingInput.getEnd(), item, user, status);

    }

    public BookingDtoForOutItemInformation mapBookingDtoToOutInformation(BookingDto bookingDto) {
        return new BookingDtoForOutItemInformation(bookingDto.getId(), bookingDto.getBooker().getId(),
                bookingDto.getStart(), bookingDto.getEnd());
    }
}
