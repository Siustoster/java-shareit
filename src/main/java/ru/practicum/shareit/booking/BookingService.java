package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.LinkedList;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper mapper;

    @Transactional
    public Booking createBooking(int userId, BookingInputDto bookingInput) {
        Item item = itemService.getFullItemById(bookingInput.getItemId());
        if (!item.getAvailable())
            throw new ItemIsUnavailableExeption("Вещь недоступна для бронирования");
        if (bookingInput.getStart() == null)
            throw new BadDateException("Некорректная дата начала бронирования");
        if (bookingInput.getEnd() == null)
            throw new BadDateException("Некорректная дата конца бронирования");
        if (bookingInput.getStart().isBefore(LocalDateTime.now()))
            throw new BadDateException("Некорректная дата начала бронирования");
        if (bookingInput.getEnd().isBefore(LocalDateTime.now()))
            throw new BadDateException("Некорректная дата конца бронирования");
        if (bookingInput.getEnd().isBefore(bookingInput.getStart()))
            throw new BadDateException("Некорректная дата конца бронирования");
        if (bookingInput.getStart().isEqual(bookingInput.getEnd()))
            throw new BadDateException("Некорректная период бронирования");
        if (itemService.getFullItemById(bookingInput.getItemId()).getUser().getId() == userId)
            throw new WrongOwnerException("Владелец не может забронировать свою вещь");

        return bookingRepository.save(mapper.mapToBooking(bookingInput, userService.getUser(userId),
                itemService.getFullItemById(bookingInput.getItemId()), BookingStatus.WAITING));
    }

    @Transactional
    public Booking approveBooking(int userId, int bookingId, String approved) {
        if (!(approved.equalsIgnoreCase("true")) && !(approved.equalsIgnoreCase("false")))
            throw new BadParameterException("Неправильный параметр approved");
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование" +
                " с айди " + bookingId
                + " не найдено"));
        if (!booking.getStatus().name().equalsIgnoreCase("WAITING"))
            throw new BadParameterException("Заявка уже рассмотрена");
        User user = userService.getUser(userId);
        if (booking.getItem().getUser().getId() != user.getId())
            if (booking.getBooker().getId() != user.getId())
                throw new UserHasNoAccess("У вас нет доступа к бронированию");
            else throw new WrongOwnerException("Вы не владелец вещи");
        if (approved.equalsIgnoreCase("true"))
            booking.setStatus(BookingStatus.APPROVED);
        else booking.setStatus(BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(int userId, int bookingId) {
        User user = userService.getUser(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование" +
                " с айди " + bookingId
                + " не найдено"));

        if (booking.getBooker().getId() == user.getId() || booking.getItem().getUser().getId() == user.getId())
            return booking;
        else
            throw new WrongOwnerException("Пользователь не является владельцем вещи " +
                    "или владельцем запроса на бронирование");

    }

    public LinkedList<Booking> getBookingByUser(int userId, String stateIn, PageRequest page) {
        User user = userService.getUser(userId);
        LinkedList<Booking> bookingLinkedList = new LinkedList<>();
        if (BookingState.findByName(stateIn) == null)
            throw new RuntimeException("Unknown state: " + stateIn);
        BookingState state = BookingState.findByName(stateIn);
        if (state.name().equalsIgnoreCase("ALL"))
            bookingLinkedList = bookingRepository.getAllUserBookings(userId, page);
        if (state.name().equalsIgnoreCase("FUTURE"))
            bookingLinkedList = bookingRepository.getAllFutureUserBookings(userId, LocalDateTime.now(), page);
        if (state.name().equalsIgnoreCase("WAITING") || state.name().equalsIgnoreCase("REJECTED"))
            bookingLinkedList = bookingRepository.getSpecialStateUserBookings(userId, BookingStatus.valueOf(stateIn), page);
        if (state.name().equalsIgnoreCase("PAST"))
            bookingLinkedList = bookingRepository.getAllPastUserBookings(userId, LocalDateTime.now(), page);
        if (state.name().equalsIgnoreCase("CURRENT"))
            bookingLinkedList = bookingRepository.getAllCurrentUserBookings(userId, LocalDateTime.now(), page);

        return bookingLinkedList;
    }

    public LinkedList<Booking> getBookingByUserItems(int userId, String stateIn, PageRequest page) {
        User user = userService.getUser(userId);
        LinkedList<Booking> bookingLinkedList = new LinkedList<>();
        if (BookingState.findByName(stateIn) == null)
            throw new RuntimeException("Unknown state: " + stateIn);
        BookingState state = BookingState.findByName(stateIn);
        if (state.name().equalsIgnoreCase("ALL"))
            bookingLinkedList = bookingRepository.getAllUserItemBookings(userId, page);
        if (state.name().equalsIgnoreCase("FUTURE"))
            bookingLinkedList = bookingRepository.getFutureUserItemBookings(userId, LocalDateTime.now(), page);
        if (state.name().equalsIgnoreCase("WAITING") || state.name().equalsIgnoreCase("REJECTED"))
            bookingLinkedList = bookingRepository.getSpecialStateUserItemBookings(userId, BookingStatus.valueOf(stateIn)
                    , page);
        if (state.name().equalsIgnoreCase("PAST"))
            bookingLinkedList = bookingRepository.getPastUserItemBookings(userId, LocalDateTime.now(), page);
        if (state.name().equalsIgnoreCase("CURRENT"))
            bookingLinkedList = bookingRepository.getCurrentUserBookings(userId, LocalDateTime.now(), page);

        return bookingLinkedList;
    }

    public Boolean checkUserUsedItem(int userId, int itemId) {
        return !bookingRepository.findFirst1ByItemIdAndBookerIdAndEndIsBefore(itemId, userId,
                LocalDateTime.now()).isEmpty();
    }

}
