package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userRepository.findById(id).get();
    }

    @Transactional
    public User updateUser(int id, User user) {
        User oldUser = getUser(id);
        if (user.getName() != null && !user.getName().isBlank())
            oldUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank())
            oldUser.setEmail(user.getEmail());

        return userRepository.save(oldUser);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.delete(getUser(id));
    }
}
