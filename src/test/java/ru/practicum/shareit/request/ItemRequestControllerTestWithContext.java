package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutWithItems;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTestWithContext {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService requestService;
    @Autowired
    private MockMvc mvc;

    User requestor = new User(1, "name", "useremail@mail.ru");
    ItemRequest itemRequest = new ItemRequest(1, "request descr", LocalDateTime.now(), requestor);
    private static final String header = "X-Sharer-User-Id";
    ItemDto itemDto = new ItemDto(1, "item1", "item1 descr", true, 1);
    ItemRequestDtoOutWithItems requestDtoOutWithItems = new ItemRequestDtoOutWithItems(1, "request descr",
            LocalDateTime.now(), requestor, List.of(itemDto));

    @Test
    void createNewRequest() throws Exception {
        when(requestService.createNewRequest(any(), anyInt()))
                .thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId())))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequest.getRequestor().getId())));

    }

    @Test
    void getAllUsersRequests() throws Exception {
        when(requestService.getAllUsersRequests(anyInt()))
                .thenReturn(List.of(requestDtoOutWithItems));
        mvc.perform(get("/requests")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(List.of(requestDtoOutWithItems)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoOutWithItems.getId())))
                .andExpect(jsonPath("$[0].description", is(requestDtoOutWithItems.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(requestDtoOutWithItems.getRequestor().getId())))
                .andExpect(jsonPath("$[0].items.length()", is(1)))
                .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId())));
    }

    @Test
    void getAllRequests() throws Exception {
        when(requestService.getAllRequests(anyInt(), any()))
                .thenReturn(List.of(requestDtoOutWithItems));
        mvc.perform(get("/requests/all")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(List.of(requestDtoOutWithItems)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoOutWithItems.getId())))
                .andExpect(jsonPath("$[0].description", is(requestDtoOutWithItems.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(requestDtoOutWithItems.getRequestor().getId())))
                .andExpect(jsonPath("$[0].items.length()", is(1)))
                .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId())));
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.getRequestById(anyInt(), anyInt()))
                .thenReturn(requestDtoOutWithItems);
        mvc.perform(get("/requests/1")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(List.of(requestDtoOutWithItems)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoOutWithItems.getId())))
                .andExpect(jsonPath("$.description", is(requestDtoOutWithItems.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(requestDtoOutWithItems.getRequestor().getId())))
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.items[0].id", is(itemDto.getId())));
    }

    @Test
    void getAllRequestsFailPage() throws Exception {
        when(requestService.getAllRequests(anyInt(), any()))
                .thenReturn(List.of(requestDtoOutWithItems));
        mvc.perform(get("/requests/all?from=-5")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(List.of(requestDtoOutWithItems)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
