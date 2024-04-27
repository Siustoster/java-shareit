package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

@AllArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User getUser(int id) {
        if (userStorage.getUser(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userStorage.getUser(id);
    }

    public User updateUser(int id, User user) {
        getUser(id);
        user.setId(id);
        return userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        getUser(id);
        userStorage.deleteUser(id);
    }
}
