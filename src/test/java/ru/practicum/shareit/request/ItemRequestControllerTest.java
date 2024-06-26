package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutWithItems;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    ItemRequestService itemRequestService;

    @InjectMocks
    ItemRequestController requestController;

    @Test
    void createRequest() {
        ItemRequestDto itemRequest = new ItemRequestDto("request");
        ItemRequest itemRequestResult = new ItemRequest(1, "request", LocalDateTime.now(), new User());
        when(itemRequestService.createNewRequest(itemRequest, 1))
                .thenReturn(itemRequestResult);
        ItemRequest result = requestController.createNewRequest(itemRequest, 1);

        assertEquals(result.getId(), itemRequestResult.getId());
        assertEquals(result.getDescription(), itemRequestResult.getDescription());
    }

    @Test
    void getAllUsersRequests() {
        ItemRequestDtoOutWithItems resultDto = new ItemRequestDtoOutWithItems(1, "descr",
                LocalDateTime.now(), new User(), null);
        List<ItemRequestDtoOutWithItems> resultList = new ArrayList<>();
        resultList.add(resultDto);
        when(itemRequestService.getAllUsersRequests(1)).thenReturn(resultList);
        List<ItemRequestDtoOutWithItems> result = requestController.getAllUsersRequests(1);
        assertEquals(result.size(), 1);
    }

    @Test
    void getAllRequests() {
        ItemRequestDtoOutWithItems resultDto = new ItemRequestDtoOutWithItems(1, "descr",
                LocalDateTime.now(), new User(), null);
        List<ItemRequestDtoOutWithItems> resultList = new ArrayList<>();
        resultList.add(resultDto);
        when(itemRequestService.getAllRequests(eq(1), any())).thenReturn(resultList);
        List<ItemRequestDtoOutWithItems> result = requestController.getAllRequests(1, 1, 5);
        assertEquals(result.size(), 1);
    }

    @Test
    void getAllRequestsFailPage() {
        ItemRequestDtoOutWithItems resultDto = new ItemRequestDtoOutWithItems(1, "descr",
                LocalDateTime.now(), new User(), null);
        List<ItemRequestDtoOutWithItems> resultList = new ArrayList<>();
        resultList.add(resultDto);
        //when(itemRequestService.getAllRequests(eq(1), any())).thenReturn(resultList);
        assertThrows(BadParameterException.class, () -> requestController.getAllRequests(1, 1, -5));
    }

    @Test
    void getAllRequestsFailPage2() {
        ItemRequestDtoOutWithItems resultDto = new ItemRequestDtoOutWithItems(1, "descr",
                LocalDateTime.now(), new User(), null);
        List<ItemRequestDtoOutWithItems> resultList = new ArrayList<>();
        resultList.add(resultDto);
        //when(itemRequestService.getAllRequests(eq(1), any())).thenReturn(resultList);
        assertThrows(BadParameterException.class, () -> requestController.getAllRequests(1, -1, 1));
    }

    @Test
    void getRequest() {

    }
}
