package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, Integer> {
    Optional<Booking> findById(Integer integer);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where u.id=?1" +
            " order by bo.start desc ")
    LinkedList<Booking> getAllUserBookings(Integer userId, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where u.id=?1 and bo.status=?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getSpecialStateUserBookings(Integer userId, BookingStatus state, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where u.id=?1 and bo.start>?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getAllFutureUserBookings(Integer userId, LocalDateTime now, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where it.user.id=?1" +
            " order by bo.start desc ")
    LinkedList<Booking> getAllUserItemBookings(Integer userId, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where it.user.id=?1 and bo.start>?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getFutureUserItemBookings(Integer userId, LocalDateTime now, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where it.user.id=?1 and bo.status=?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getSpecialStateUserItemBookings(Integer userId, BookingStatus now, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where u.id=?1 and bo.end<?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getAllPastUserBookings(Integer userId, LocalDateTime now, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where u.id=?1 and bo.end>?2 and bo.start<?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getAllCurrentUserBookings(Integer userId, LocalDateTime now, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where it.user.id=?1 and bo.end<?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getPastUserItemBookings(Integer userId, LocalDateTime now, PageRequest page);

    @Query("Select bo from Booking as bo" +
            " join bo.booker as u " +
            " join bo.item as it" +
            " where it.user.id=?1 and bo.end>?2 and bo.start<?2" +
            " order by bo.start desc ")
    LinkedList<Booking> getCurrentUserBookings(Integer userId, LocalDateTime now, PageRequest page);

    List<BookingDto> findFirst1ByItemIdAndStartIsBeforeAndStatusOrderByStartDesc(Integer itemId,
                                                                                 LocalDateTime start,
                                                                                 BookingStatus status);

    List<BookingDto> findFirst1ByItemIdAndStartIsAfterAndStatusOrderByStartAsc(Integer itemId,
                                                                               LocalDateTime start,
                                                                               BookingStatus status);

    List<Booking> findFirst1ByItemIdAndBookerIdAndEndIsBefore(Integer itemId, Integer userId, LocalDateTime end);
}
