package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage inMemoryUserStorage;

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAll();
    }

    public User addUser(User user) {
        return inMemoryUserStorage.add(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.update(user);
    }

    public User getUserById(Integer id) {
        return  inMemoryUserStorage.getById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void removeFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> getFriends(Integer id) {
        User user = getUserById(id);
        return user.getFriends().stream().map(this::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        return user.getFriends().stream().filter(otherUser.getFriends()::contains)
                .map(this::getUserById).collect(Collectors.toList());
    }
}
