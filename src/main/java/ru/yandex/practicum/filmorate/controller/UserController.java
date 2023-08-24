package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Добавление пользователя: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@Valid @PositiveOrZero @PathVariable Integer id) {
        log.info("Получение пользователя по id: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@Valid @PositiveOrZero @PathVariable Integer id,
                          @Valid @PositiveOrZero @PathVariable Integer friendId) {
        log.info("Добавление пользователя friendId: {} в друзья пользователя id: {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@Valid @PositiveOrZero @PathVariable Integer id,
                             @Valid @PositiveOrZero @PathVariable Integer friendId) {
        log.info("Удаление пользователя friendId: {} из друзей пользователя id: {}", friendId, id);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@Valid @PositiveOrZero @PathVariable Integer id) {
        log.info("Получение списка друзей пользователя id: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@Valid @PositiveOrZero @PathVariable Integer id,
                                       @Valid @PositiveOrZero @PathVariable Integer otherId) {
        log.info("Получение списка общих друзей пользователей id: {} и otherId: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
