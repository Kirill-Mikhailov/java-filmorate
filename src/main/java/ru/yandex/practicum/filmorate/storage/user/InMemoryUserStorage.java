package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int id;
    private final HashMap<Integer, User> users = new HashMap<>();

    private int getNewId() {
        return ++id;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        user.setId(getNewId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Обновление пользователя c неверным id: {}", user);
            throw new UserNotFoundException("Пользователя с id " + user.getId() + " не существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(Integer id) {
        if (!users.containsKey(id)) {
            log.debug("Получение пользователя с неверным id: {}", id);
            throw new UserNotFoundException("Пользователя с id " + id + " не существует");
        }
        return users.get(id);
    }
}
