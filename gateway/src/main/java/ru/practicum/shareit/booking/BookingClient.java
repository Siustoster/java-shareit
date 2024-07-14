package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.client.BaseClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBooking(Integer userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookings(Integer userId, BookingState state, Integer from, Integer size,
                                              boolean forOwner) {
        StringBuilder path = new StringBuilder("/");

        if (forOwner) {
            path.append("owner");
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("state", state.name());
        path.append("?state={state}");

        if (from != null && size != null) {
            parameters.put("from", from);
            parameters.put("size", size);
            path.append("&from={from}&size={size}");
        }

        return get(path.toString(), userId, parameters);
    }

    public ResponseEntity<Object> addBooking(Integer userId, BookingInputDto booking) {
        return post("", userId, booking);
    }

    public ResponseEntity<Object> approveBooking(Integer userId, Integer bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved.toString()
        );

        return patch(String.format("/%s?approved={approved}", bookingId), userId, parameters, null);
    }
}
