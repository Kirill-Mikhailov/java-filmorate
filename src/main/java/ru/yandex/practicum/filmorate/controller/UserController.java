package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private int id = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    private int getNewId() {
        return this.id++;
    }

    @GetMapping
    public List<User> allUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Добавление пользователя: {}", user);
        user.setId(getNewId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        if (!users.containsKey(user.getId())) {
            log.debug("Обновление пользователя c неверным id: {}", user);
            throw new ValidationException("Пользователя с id " + user.getId() + " не существует");
        }
        users.put(user.getId(), user);
        return user;
    }
}
