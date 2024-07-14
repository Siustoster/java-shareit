package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private static final String USER = "X-Sharer-User-Id";

    private final RequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(USER) Integer userId,
                                                 @RequestBody @Valid ItemRequestDto requestDto) {
        return itemRequestClient.addItemRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequest(@RequestHeader(USER) Integer userId) {
        return itemRequestClient.getUserItemRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(USER) Integer userId,
                                                     @PathVariable Integer requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsPageable(
            @RequestHeader(USER) Integer userId,
            @RequestParam(required = false) @Min(0) Integer from,
            @RequestParam(required = false) @Min(1) @Max(20) Integer size
    ) {
        return itemRequestClient.getItemRequestsPageable(userId, from, size);
    }
}
