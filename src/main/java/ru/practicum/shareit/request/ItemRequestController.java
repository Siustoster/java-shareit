package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutWithItems;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequest createNewRequest(@RequestBody @Valid ItemRequestDto request,
                                        @RequestHeader(header) int userId) {
        return requestService.createNewRequest(request, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOutWithItems> getAllUsersRequests(@RequestHeader(header) int userId) {
        return requestService.getAllUsersRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOutWithItems> getAllRequests(@RequestHeader(header) int userId,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size < 1)
            throw new BadParameterException("Неверные параметры страницы");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("created").ascending());
        return requestService.getAllRequests(userId, page);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOutWithItems getRequest(@RequestHeader(header) int userId,
                                                 @PathVariable int requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}
