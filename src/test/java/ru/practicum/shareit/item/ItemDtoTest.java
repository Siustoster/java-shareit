package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {
    private static final String TEXT = "text";
    @Autowired
    private JacksonTester<ItemDto> itemDtoJson;

    @SneakyThrows
    @Test
    void testItemDtoSerialize() {
        ItemDto itemDto = new ItemDto(1, "name", "description", true, null);
        JsonContent<ItemDto> actual = itemDtoJson.write(itemDto);
        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.name");
        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).hasJsonPath("$.available");
    }
}
