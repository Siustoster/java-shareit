package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.BadParameterException;

public class PageableUtility {
    public static PageRequest setPage(int from, int size) {
        if (from < 0 || size < 1)
            throw new BadParameterException("Неверные параметры страницы");
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public static PageRequest setPageSorted(int from, int size) {
        if (from < 0 || size < 1)
            throw new BadParameterException("Неверные параметры страницы");
        return PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("created").ascending());
    }
}
