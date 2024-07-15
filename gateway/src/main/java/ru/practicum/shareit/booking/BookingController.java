package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingInputDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
@Validated
public class BookingController {
    private static final String USER = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER) Integer userId,
                                             @PathVariable Integer bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(USER) Integer userId,
                                                      @RequestParam(defaultValue = "ALL") BookingState state,
                                                      @RequestParam(required = false) @Min(0) Integer from,
                                                      @RequestParam(required = false) @Min(1) @Max(20) Integer size) {
        return bookingClient.getBookings(userId, state, from, size, false);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(USER) Integer userId,
                                                     @RequestParam(defaultValue = "ALL") BookingState state,
                                                     @RequestParam(required = false) @Min(0) Integer from,
                                                     @RequestParam(required = false) @Min(1) @Max(20) Integer size) {
        return bookingClient.getBookings(userId, state, from, size, true);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER) Integer userId,
                                             @RequestBody @Valid BookingInputDto bookingDto) {
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER) Integer userId,
                                                 @PathVariable Integer bookingId,
                                                 @RequestParam @NotNull Boolean approved) {
        return bookingClient.approveBooking(userId, bookingId, approved);
    }
}