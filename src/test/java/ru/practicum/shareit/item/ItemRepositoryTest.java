package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "user", "user@email.com");
        userRepository.save(user1);
        user2 = new User(null, "user_2", "user_2@email.com");
        userRepository.save(user2);
        item1 = new Item(null, "name_item_1", "description_1", true, user1, null);
        itemRepository.save(item1);
        item2 = new Item(null, "name_item_2", "description_2", true, user1, null);
        itemRepository.save(item2);
    }

    @Test
    void getItem() {
        Optional<Item> foundedItem = itemRepository.findById(item1.getId());
        assertThat(foundedItem.get(), notNullValue());
    }

    @Test
    void findByIdAndUserId() {
        List<Item> itemFounded = itemRepository.findByIdAndUserId(item1.getId(), user1.getId());
        assertThat(itemFounded.size(), equalTo(1));
    }

    @Test
    void getAllUserItems() {
        List<Item> itemFounded = itemRepository.getAllUserItems(user1.getId());
        assertThat(itemFounded.size(), equalTo(2));
    }

    @Test
    void findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue() {
        List<Item> itemFounded = itemRepository
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(item1.getName(),
                        item1.getName());
        assertThat(itemFounded.size(), equalTo(1));
    }
}
