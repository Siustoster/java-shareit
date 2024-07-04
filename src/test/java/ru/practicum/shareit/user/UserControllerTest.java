package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

    @Test
    void createUser() {
        User user = new User(null, "user", "userEmail");
        User userCreated = new User(1, "user", "userEmail");

        when(userService.createUser(user))
                .thenReturn(userCreated);

        User response = userController.createUser(user);

        assertEquals(response.getId(), userCreated.getId());
        assertEquals(response.getName(), userCreated.getName());
        assertEquals(response.getEmail(), userCreated.getEmail());
    }
}
