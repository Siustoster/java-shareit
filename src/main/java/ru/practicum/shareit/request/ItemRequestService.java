package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
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
        return getRequestsAndItems(requests);
    }

    public List<ItemRequestDtoOutWithItems> getAllRequests(int userId, PageRequest page) {
        User requestor = userService.getUser(userId);
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdNot(userId, page);
        return getRequestsAndItems(requests);
    }

    public ItemRequestDtoOutWithItems getRequestById(int userId, int requestId) {
        User requestor = userService.getUser(userId);
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос"));
        return getRequestsAndItems(List.of(request)).get(0);
    }

    protected List<ItemRequestDtoOutWithItems> getRequestsAndItems(List<ItemRequest> requests) {
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
