package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userDbStorage;

    public List<User> getAllUsers() {
        return userDbStorage.getAll();
    }

    public User addUser(User user) {
        return userDbStorage.add(user);
    }

    public User updateUser(User user) {
        return userDbStorage.update(user);
    }

    public User getUserById(Integer id) {
        return userDbStorage.getById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        getUserById(id); // Для проверки на наличие пользователя с таким id
        getUserById(friendId); // Для проверки на наличие пользователя с таким id
        userDbStorage.addFriend(id, friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        getUserById(id); // Для проверки на наличие пользователя с таким id
        getUserById(friendId); // Для проверки на наличие пользователя с таким id
        userDbStorage.removeFriend(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        getUserById(id); // Для проверки на наличие пользователя с таким id
        return userDbStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        getUserById(id); // Для проверки на наличие пользователя с таким id
        getUserById(otherId); // Для проверки на наличие пользователя с таким id
        return userDbStorage.getCommonFriends(id, otherId);
    }
}
