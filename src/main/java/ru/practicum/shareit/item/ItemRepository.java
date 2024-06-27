package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findById(Integer id);

    @Query("Select it from Item as it" +
            " join it.user as u" +
            " where it.id =?1 and u.id = ?2 ")
    List<Item> findByIdAndUserId(Integer id, Integer userId);

    @Query("Select it from Item as it" +
            " join it.user as u" +
            " where u.id=?1")
    List<Item> getAllUserItems(Integer userId);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text,
                                                                                                    String text2);

    List<Item> findAllByRequestIdIn(List<Integer> requestId);
}
