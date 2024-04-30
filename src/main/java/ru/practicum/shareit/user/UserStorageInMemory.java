package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;

import java.util.*;

@Service
public class UserStorageInMemory implements UserStorage {
    private int idCount = 0;
    private List<String> emails = new ArrayList<>();
    private LinkedHashMap<Integer, User> userList = new LinkedHashMap<>();

    @Override
    public User createUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new EmailAlreadyExistsException("Пользователь с имейлом " + user.getEmail() + " уже существует");
        }
        emails.add(user.getEmail());
        idCount++;
        user.setId(idCount);
        userList.put(idCount, user);
        return userList.get(idCount);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userList.values();
    }

    @Override
    public User getUser(int id) {
        return userList.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (user.getEmail() != null)
            emails.remove(userList.get(user.getId()).getEmail());
        if (emails.contains(user.getEmail())) {
            emails.add(userList.get(user.getId()).getEmail());
            throw new EmailAlreadyExistsException("Пользователь с имейлом " + user.getEmail() + " уже существует");
        }

        if (user.getEmail() != null) {
            userList.get(user.getId()).setEmail(user.getEmail());
            emails.add(user.getEmail());
        }
        if (user.getName() != null) {
            userList.get(user.getId()).setName(user.getName());
        }

        return userList.get(user.getId());
    }

    @Override
    public void deleteUser(int id) {
        emails.remove(userList.get(id).getEmail());
        userList.remove(id);
    }
}
