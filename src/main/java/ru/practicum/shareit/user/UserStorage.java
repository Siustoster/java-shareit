package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);

    Collection<User> getAllUsers();

    User getUser(int id);

    User updateUser(User user);

    void deleteUser(int id);

}
