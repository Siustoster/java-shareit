package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutWithItems;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestMapper requestMapper;

    public ItemRequest createNewRequest(ItemRequestDto itemRequestDto, int userId) {
        User user = userService.getUser(userId);
        ItemRequest itemRequest = requestMapper.mapInputDtoToItemRequest(itemRequestDto, user);

        return requestRepository.save(itemRequest);
    }

    public List<ItemRequestDtoOutWithItems> getAllUsersRequests(int userId) {
        User requestor = userService.getUser(userId);
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdOrderByCreatedAsc(userId);
        if (requests.isEmpty())
            return requests.stream().map(request -> requestMapper.mapRequestToDtoOutWithItems(request, new ArrayList<ItemDto>()))
                    .collect(Collectors.toList());
        else {
            List<Integer> requestsId = requests.stream().map(ItemRequest::getId).collect(Collectors.toList());
            List<ItemDto> items = itemService.getAllItemsByRequestsList(requestsId);
            if (items.isEmpty())
                return requests.stream().map(request -> requestMapper.mapRequestToDtoOutWithItems(request, new ArrayList<ItemDto>()))
                        .collect(Collectors.toList());
            else {
                return requestMapper.mapRequestsAndItemsInList(requests, items);
            }
        }
    }
}
