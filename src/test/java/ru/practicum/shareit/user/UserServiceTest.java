package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private final User user1 = new User(null, "user1", "user1email@mail.ru");
    private final User user2 = new User(null, "user2", "user2email@mail.ru");

    @Test
    void getAllUsers() {
        User savedUser1 = userService.createUser(user1);
        User savedUser2 = userService.createUser(user2);
        List<User> userList = userService.getAllUsers();
        assertThat(userList.size(), equalTo(2));

    }

    @Test
    void saveUser() {
        User savedUser1 = userService.createUser(user1);
        assertThat(savedUser1.getId(), notNullValue());
        assertThat(savedUser1.getName(), equalTo(user1.getName()));
        assertThat(savedUser1.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    void getUser() {
        User savedUser1 = userService.createUser(user1);
        User gettedUser = userService.getUser(savedUser1.getId());
        assertThat(gettedUser.getName(), equalTo(user1.getName()));
        assertThat(gettedUser.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    void updateUser() {
        User savedUser1 = userService.createUser(user1);
        User updatedUser = userService.updateUser(savedUser1.getId(), user2);
        assertThat(updatedUser.getName(), equalTo(user2.getName()));
        assertThat(updatedUser.getEmail(), equalTo(user2.getEmail()));
        assertThat(savedUser1.getId(), equalTo(updatedUser.getId()));
    }

    @Test
    void deleteUser() {
        User savedUser = userService.createUser(user1);
        userService.deleteUser(savedUser.getId());
        assertThrows(NotFoundException.class, () -> userService.getUser(savedUser.getId()));
    }
}
