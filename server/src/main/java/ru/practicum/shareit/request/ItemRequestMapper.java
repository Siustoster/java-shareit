package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutWithItems;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {
    public ItemRequest mapInputDtoToItemRequest(ItemRequestDto itemRequestDto, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public ItemRequestDtoOutWithItems mapRequestToDtoOutWithItems(ItemRequest request, List<ItemDto> items) {
        return new ItemRequestDtoOutWithItems(request.getId(), request.getDescription(), request.getCreated(),
                request.getRequestor(), items);
    }

    public LinkedList<ItemRequestDtoOutWithItems> mapRequestsAndItemsInList(List<ItemRequest> requests,
                                                                            List<ItemDto> items) {
        List<ItemDto> foundedItems;
        LinkedList<ItemRequestDtoOutWithItems> mappedRequests = new LinkedList<>();
        for (ItemRequest request : requests) {
            foundedItems = items.stream()
                    .filter(itemDto -> itemDto.getRequestId() == request.getId())
                    .collect(Collectors.toList());
            mappedRequests.add(mapRequestToDtoOutWithItems(request, foundedItems));
        }
        return mappedRequests;
    }
}
