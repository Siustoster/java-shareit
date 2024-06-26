package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exception.BadParameterException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String header = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public Booking createBookingRequest(@RequestHeader(header) int userId,
                                        @RequestBody @Valid BookingInputDto booking) {
        return bookingService.createBooking(userId, booking);
    }

    @PatchMapping({"/{bookingId}"})
    public Booking approveBooking(@RequestHeader(header) int userId,
                                  @PathVariable int bookingId,
                                  @RequestParam(defaultValue = "false") String approved) {
        return bookingService.approveBooking(userId, bookingId, Boolean.valueOf(approved));
    }

    @GetMapping({"/{bookingId}"})
    public Booking getBookingById(@RequestHeader(header) int userId,
                                  @PathVariable int bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getAllBookingsOfCurrentUser(@RequestHeader(header) int userId,
                                                     @RequestParam(defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size < 1)
            throw new BadParameterException("Неверные параметры страницы");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return bookingService.getBookingByUser(userId, state, page);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsForAllUserItems(@RequestHeader(header) int userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size < 1)
            throw new BadParameterException("Неверные параметры страницы");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return bookingService.getBookingByUserItems(userId, state, page);
    }
}
