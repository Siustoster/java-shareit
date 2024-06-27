package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoForOutItemInformation;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTestWithContext {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @Autowired
    private MockMvc mvc;

    private LocalDateTime testDate = LocalDateTime.of(2024,
            6, 30, 9, 0, 0);
    private BookingDtoForOutItemInformation booking = new BookingDtoForOutItemInformation(1, 1,
            testDate, testDate);
    private CommentDto comment = new CommentDto(1, "testComment", "Author", testDate);
    private User user = new User(1, "Name", "email@mail.com");
    private User user2 = new User(2, "Name2", "email2@mail.com");
    private static final String header = "X-Sharer-User-Id";
    private ItemDto itemDto1 = new ItemDto(1, "item1", "item1descr", true, null);
    private ItemDtoWithBookingAndComment itemFull = new ItemDtoWithBookingAndComment(1, "item1",
            "item1descr", true, booking, booking, List.of(comment));
    private ItemDtoWithBooking itemDtoWithBooking = new ItemDtoWithBooking(itemFull.getId(), itemFull.getName(),
            itemFull.getDescription(), itemFull.getAvailable(), itemFull.getLastBooking(), itemFull.getNextBooking());

    @Test
    void createNewItem() throws Exception {
        when(itemService.createItem(itemDto1, user.getId()))
                .thenReturn(itemDto1);

        mvc.perform(post("/items")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId())))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(itemDto1, itemDto1.getId(), user.getId()))
                .thenReturn(itemDto1);

        mvc.perform(patch("/items/1")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId())))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemByIdAndUserId(anyInt(), anyInt()))
                .thenReturn(itemFull);

        mvc.perform(get("/items/1")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(itemFull))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemFull.getId())))
                .andExpect(jsonPath("$.name", is(itemFull.getName())))
                .andExpect(jsonPath("$.description", is(itemFull.getDescription())))
                .andExpect(jsonPath("$.available", is(itemFull.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(booking.getId())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(booking.getBookerId())))
                .andExpect(jsonPath("$.lastBooking.end", is(booking.getEnd().toString() + ":00")))
                .andExpect(jsonPath("$.lastBooking.start", is(booking.getStart() + ":00")))
                .andExpect(jsonPath("$.nextBooking.id", is(booking.getId())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(booking.getBookerId())))
                //.andExpect(jsonPath("$.nextBooking.end", is(booking.getEnd())))
                //.andExpect(jsonPath("$.nextBooking.start", is(booking.getStart())))
                .andExpect(jsonPath("$.comments[0].id", is(comment.getId())))
                .andExpect(jsonPath("$.comments[0].text", is(comment.getText())))
                //.andExpect(jsonPath("$.comments[0].created", is(comment.getCreated())))
                .andExpect(jsonPath("$.comments[0].authorName", is(comment.getAuthorName())));


    }

    @Test
    void getAllUserItems() throws Exception {
        when(itemService.getAllUserItems(anyInt()))
                .thenReturn(List.of(itemDtoWithBooking));

        mvc.perform(get("/items")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(List.of(itemDtoWithBooking)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemFull.getId())))
                .andExpect(jsonPath("$[0].name", is(itemFull.getName())))
                .andExpect(jsonPath("$[0].description", is(itemFull.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemFull.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(booking.getId())))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(booking.getBookerId())))

                .andExpect(jsonPath("$[0].nextBooking.id", is(booking.getId())))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(booking.getBookerId())));

    }

    @Test
    void searchItem() throws Exception {
        when(itemService.searchItem(anyString(), anyInt()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items/search?text=Какжеязаебался")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(List.of(itemDto1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId())))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto1.getAvailable())));
    }

    @Test
    void postComment() throws Exception {
        when(itemService.postComment(any(CommentDto.class), anyInt(), anyInt()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())));

    }

}
