package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByRequestorIdOrderByCreatedAsc(int requestorId);

    List<ItemRequest> findAllByRequestorIdNot(int requestorId, PageRequest page);
}
